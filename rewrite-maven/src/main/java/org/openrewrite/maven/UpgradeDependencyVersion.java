/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.maven;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.*;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.tree.*;
import org.openrewrite.semver.Semver;
import org.openrewrite.semver.VersionComparator;
import org.openrewrite.xml.AddToTagVisitor;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.*;

import static org.openrewrite.internal.StringUtils.matchesGlob;

/**
 * Upgrade the version of a dependency by specifying a group or group and artifact using Node Semver
 * <a href="https://github.com/npm/node-semver#advanced-range-syntax">advanced range selectors</a>, allowing
 * more precise control over version updates to patch or minor releases.
 * <P><P>
 * NOTES:
 * <li>If a version is defined as a property, this recipe will only change the property value if the property exists within the same pom.</li>
 * <li>This recipe will alter the managed version of the dependency if it exists in the pom.</li>
 * <li>The default behavior for managed dependencies is to leave them unaltered unless the "overrideManagedVersion" is set to true.</li>
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class UpgradeDependencyVersion extends Recipe {

    @Option(displayName = "Group",
            description = "The first part of a dependency coordinate `com.google.guava:guava:VERSION`. This can be a glob expression.",
            example = "com.fasterxml.jackson*")
    String groupId;

    @Option(displayName = "Artifact",
            description = "The second part of a dependency coordinate `com.google.guava:guava:VERSION`. This can be a glob expression.",
            example = "jackson-module*")
    String artifactId;

    @Option(displayName = "New version",
            description = "An exact version number or node-style semver selector used to select the version number.",
            example = "29.X")
    String newVersion;

    @Option(displayName = "Version pattern",
            description = "Allows version selection to be extended beyond the original Node Semver semantics. So for example," +
                    "Setting 'version' to \"25-29\" can be paired with a metadata pattern of \"-jre\" to select Guava 29.0-jre",
            example = "-jre",
            required = false)
    @Nullable
    String versionPattern;

    @Option(displayName = "Override managed version",
            description = "This flag can be set to explicitly override a managed dependency's version. The default for this flag is `false`.",
            example = "false",
            required = false)
    @Nullable
    Boolean overrideManagedVersion;

    @SuppressWarnings("ConstantConditions")
    @Override
    public Validated validate() {
        Validated validated = super.validate();
        if (newVersion != null) {
            validated = validated.and(Semver.validate(newVersion, versionPattern));
        }
        return validated;
    }

    @Override
    public String getDisplayName() {
        return "Upgrade Maven dependency version";
    }

    @Override
    public String getDescription() {
        return "Upgrade the version of a dependency by specifying a group or group and artifact using Node Semver " +
                "advanced range selectors, allowing more precise control over version updates to patch or minor releases.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        VersionComparator versionComparator = Semver.validate(newVersion, versionPattern).getValue();
        assert versionComparator != null;

        return new MavenIsoVisitor<ExecutionContext>() {
            private final Map<GroupArtifact, List<String>> availableVersions = new HashMap<>();

            @Override
            public Xml.Document visitDocument(Xml.Document document, ExecutionContext ctx) {
                Xml.Document d = super.visitDocument(document, ctx);

                if (d != document) {
                    maybeUpdateModel();
                    doAfterVisit(new RemoveRedundantDependencyVersions(null, null, true));
                }
                return d;
            }

            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext ctx) {
                Xml.Tag t = super.visitTag(tag, ctx);
                if (isDependencyTag(groupId, artifactId)) {

                    ResolvedDependency d = findDependency(tag);
                    if (d != null) {
                        String newerVersion = findNewerVersion(d.getGroupId(), d.getArtifactId(), d.getVersion(), ctx);
                        if (newerVersion != null) {
                            ResolvedManagedDependency dm = findManagedDependency(t);
                            if (dm != null) {
                                String requestedVersion = dm.getRequested().getVersion();
                                if (requestedVersion.startsWith("${")) {
                                    doAfterVisit(new ChangePropertyValue(requestedVersion.substring(2, requestedVersion.length() - 1), newerVersion, overrideManagedVersion));
                                    return t;
                                }
                            }

                            Optional<Xml.Tag> version = t.getChild("version");
                            if (version.isPresent()) {
                                String requestedVersion = d.getRequested().getVersion();
                                if (requestedVersion != null && requestedVersion.startsWith("${")) {
                                    doAfterVisit(new ChangePropertyValue(requestedVersion.substring(2, requestedVersion.length() - 1), newerVersion, overrideManagedVersion));
                                    return t;
                                }
                                t = (Xml.Tag) new ChangeTagValueVisitor<Integer>(version.get(), newerVersion).visitNonNull(t, 0, getCursor());
                            } else if (Boolean.TRUE.equals(overrideManagedVersion)) {
                                //If the version is not present and the override managed version is set, add a new, explicit version tag.
                                Xml.Tag versionTag = Xml.Tag.build("<version>" + newerVersion + "</version>");
                                //noinspection ConstantConditions
                                t = (Xml.Tag) new AddToTagVisitor<ExecutionContext>(t, versionTag, new MavenTagInsertionComparator(t.getChildren())).visitNonNull(t, ctx, getCursor().getParent());
                            }
                        }
                    }
                } else if (isManagedDependencyTag(groupId, artifactId)) {

                    ResolvedManagedDependency matchedManagedDependency = findManagedDependency(t);
                    if (matchedManagedDependency != null) {
                        if (matchesGlob(matchedManagedDependency.getGroupId(), groupId) && matchesGlob(matchedManagedDependency.getArtifactId(), artifactId)) {
                            String requestedVersion = matchedManagedDependency.getRequested().getVersion();
                            assert (matchedManagedDependency.getVersion() != null);
                            String newerVersion = findNewerVersion(matchedManagedDependency.getGroupId(), matchedManagedDependency.getArtifactId(), matchedManagedDependency.getVersion(), ctx);
                            if (newerVersion != null) {
                                if (requestedVersion.startsWith("${")) {
                                    doAfterVisit(new ChangePropertyValue(requestedVersion.substring(2, requestedVersion.length() - 1), newerVersion, overrideManagedVersion));
                                    return t;
                                }
                                Xml.Tag childVersionTag = t.getChild("version").orElse(null);
                                if (childVersionTag != null) {
                                    t = (Xml.Tag) new ChangeTagValueVisitor<Integer>(childVersionTag, newerVersion).visitNonNull(t, 0, getCursor());
                                }
                            }
                        }
                    } else {
                        for (ResolvedManagedDependency dm : getResolutionResult().getPom().getDependencyManagement()) {
                            if (dm.getBomGav() != null) {
                                ResolvedGroupArtifactVersion bom = dm.getBomGav();
                                String tagGroup = getResolutionResult().getPom().getValue(tag.getChildValue("groupId").orElse(getResolutionResult().getPom().getGroupId()));
                                String tagArtifactId = getResolutionResult().getPom().getValue(tag.getChildValue("artifactId").orElse(""));

                                if (tagGroup != null && tagArtifactId != null && tagGroup.equals(bom.getGroupId()) && tagArtifactId.equals(bom.getArtifactId())) {

                                    //noinspection ConstantConditions
                                    String requestedVersion = dm.getRequestedBom().getVersion();
                                    String newerVersion = findNewerVersion(bom.getGroupId(), bom.getArtifactId(), bom.getVersion(), ctx);
                                    if (newerVersion != null) {
                                        if (requestedVersion.startsWith("${")) {
                                            doAfterVisit(new ChangePropertyValue(requestedVersion.substring(2, requestedVersion.length() - 1), newerVersion, overrideManagedVersion));
                                            return t;
                                        }
                                        Xml.Tag childVersionTag = t.getChild("version").orElse(null);
                                        if (childVersionTag != null) {
                                            t = (Xml.Tag) new ChangeTagValueVisitor<Integer>(childVersionTag, newerVersion).visitNonNull(t, 0, getCursor());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                return t;
            }

            @Nullable
            private String findNewerVersion(String groupId, String artifactId, String version, ExecutionContext ctx) {
                GroupArtifact ga = new GroupArtifact(groupId, artifactId);
                try {
                    List<String> artifactVersions = availableVersions.computeIfAbsent(ga,
                            k -> {
                                MavenMetadata mavenMetadata = downloadMetadata(groupId, artifactId, ctx);
                                List<String> versions = new ArrayList<>();
                                for (String v : mavenMetadata.getVersioning().getVersions()) {
                                    if (versionComparator.isValid(version, v)) {
                                        versions.add(v);
                                    }
                                }
                                return versions;
                            });
                    return versionComparator.upgrade(version, artifactVersions).orElse(null);
                } catch (IllegalStateException e) {
                    //This can happen when we encounter exotic version. Pass the error to the error handler and
                    //in the spirit of "do no harm", return null.
                    ctx.getOnError().accept(e);
                    return null;
                }
            }
        };
    }
}
