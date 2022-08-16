/*
 * Copyright 2021 the original author or authors.
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
package org.openrewrite.gradle

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.openrewrite.gradle.Assertions.buildGradle
import org.openrewrite.test.RewriteTest

class ChangeDependencyGroupIdTest : RewriteTest {
    @Test
    fun worksWithEmptyStringConfig() = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId("org.springframework.boot", "spring-boot-starter", "org.newboot", ""))
        },
        buildGradle(
            """
                dependencies {
                    rewrite 'org.openrewrite:rewrite-gradle:latest.integration'
                    implementation 'org.springframework.cloud:spring-cloud-starter-sleuth:3.0.3'
                    implementation 'org.springframework.integration:spring-integration-ftp:5.5.1'
                    implementation 'org.springframework.boot:spring-boot-starter:2.5.4'
                    implementation 'commons-lang:commons-lang:2.6'
                    testImplementation 'org.springframework.boot:spring-boot-starter-test'
                }
            """,
            """
                dependencies {
                    rewrite 'org.openrewrite:rewrite-gradle:latest.integration'
                    implementation 'org.springframework.cloud:spring-cloud-starter-sleuth:3.0.3'
                    implementation 'org.springframework.integration:spring-integration-ftp:5.5.1'
                    implementation 'org.newboot:spring-boot-starter:2.5.4'
                    implementation 'commons-lang:commons-lang:2.6'
                    testImplementation 'org.springframework.boot:spring-boot-starter-test'
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun findDependency(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId(group, artifact, "org.dewrite", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release'
                    api "org.openrewrite:rewrite-core:latest.release"
                }
            """,
            """
                dependencies {
                    api 'org.dewrite:rewrite-core:latest.release'
                    api "org.dewrite:rewrite-core:latest.release"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun findMapStyleDependency(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId(group, artifact, "org.dewrite", null))
        },
        buildGradle(
            """
                dependencies {
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release"
                }
            """,
            """
                dependencies {
                    api group: 'org.dewrite', name: 'rewrite-core', version: 'latest.release'
                    api group: "org.dewrite", name: "rewrite-core", version: "latest.release"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithoutVersion(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId(group, artifact, "org.dewrite", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core'
                    api "org.openrewrite:rewrite-core"
                    api group: 'org.openrewrite', name: 'rewrite-core'
                    api group: "org.openrewrite", name: "rewrite-core"
                }
            """,
            """
                dependencies {
                    api 'org.dewrite:rewrite-core'
                    api "org.dewrite:rewrite-core"
                    api group: 'org.dewrite', name: 'rewrite-core'
                    api group: "org.dewrite", name: "rewrite-core"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithClassifier(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId(group, artifact, "org.dewrite", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release:classifier'
                    api "org.openrewrite:rewrite-core:latest.release:classifier"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier"
                }
            """,
            """
                dependencies {
                    api 'org.dewrite:rewrite-core:latest.release:classifier'
                    api "org.dewrite:rewrite-core:latest.release:classifier"
                    api group: 'org.dewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier'
                    api group: "org.dewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithExt(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyGroupId(group, artifact, "org.dewrite", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core@ext'
                    api "org.openrewrite:rewrite-core@ext"
                    api 'org.openrewrite:rewrite-core:latest.release@ext'
                    api "org.openrewrite:rewrite-core:latest.release@ext"
                    api 'org.openrewrite:rewrite-core:latest.release:classifier@ext'
                    api "org.openrewrite:rewrite-core:latest.release:classifier@ext"
                    api group: 'org.openrewrite', name: 'rewrite-core', ext: 'ext'
                    api group: "org.openrewrite", name: "rewrite-core", ext: "ext"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', ext: 'ext'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", ext: "ext"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier', ext: 'ext'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier", ext: "ext"
                }
            """,
            """
                dependencies {
                    api 'org.dewrite:rewrite-core@ext'
                    api "org.dewrite:rewrite-core@ext"
                    api 'org.dewrite:rewrite-core:latest.release@ext'
                    api "org.dewrite:rewrite-core:latest.release@ext"
                    api 'org.dewrite:rewrite-core:latest.release:classifier@ext'
                    api "org.dewrite:rewrite-core:latest.release:classifier@ext"
                    api group: 'org.dewrite', name: 'rewrite-core', ext: 'ext'
                    api group: "org.dewrite", name: "rewrite-core", ext: "ext"
                    api group: 'org.dewrite', name: 'rewrite-core', version: 'latest.release', ext: 'ext'
                    api group: "org.dewrite", name: "rewrite-core", version: "latest.release", ext: "ext"
                    api group: 'org.dewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier', ext: 'ext'
                    api group: "org.dewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier", ext: "ext"
                }
            """
        )
    )
}
