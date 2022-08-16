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
package org.openrewrite.java.cleanup;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.NameCaseConvention;
import org.openrewrite.internal.StringUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.ChangeMethodName;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Value
@EqualsAndHashCode(callSuper = true)
public class MethodNameCasing extends Recipe {

    @Option(displayName = "Apply recipe to test source set",
            description = "Changes only apply to main by default. `includeTestSources` will apply the recipe to `test` source files.",
            required = false,
            example = "true")
    @Nullable
    Boolean includeTestSources;

    @Override
    public String getDisplayName() {
        return "Method name casing";
    }

    @Override
    public String getDescription() {
        return "Method names should comply with a naming convention.";
    }

    @Override
    public Set<String> getTags() {
        return Collections.singleton("RSPEC-100");
    }

    @Override
    public Duration getEstimatedEffortPerOccurrence() {
        return Duration.ofMinutes(5);
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        Pattern standardMethodName = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
        Pattern snakeCase = Pattern.compile("^[a-zA-Z0-9_]*$");
        return new JavaIsoVisitor<ExecutionContext>() {

            @Override
            public J.CompilationUnit visitCompilationUnit(J.CompilationUnit cu, ExecutionContext executionContext) {
                Optional<JavaSourceSet> sourceSet = cu.getMarkers().findFirst(JavaSourceSet.class);
                if (sourceSet.isPresent() && (Boolean.TRUE.equals(includeTestSources) || "main".equals(sourceSet.get().getName()))) {
                    return super.visitCompilationUnit(cu, executionContext);
                }
                return cu;
            }

            @Override
            public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
                if (method.getMethodType() != null &&
                        getCursor().firstEnclosingOrThrow(J.ClassDeclaration.class).getType() != null &&
                        !method.isConstructor() &&
                        !TypeUtils.isOverride(method.getMethodType()) &&
                        !standardMethodName.matcher(method.getSimpleName()).matches()) {
                    StringBuilder standardized = new StringBuilder();
                    char[] name = method.getSimpleName().toCharArray();

                    if (snakeCase.matcher(method.getSimpleName()).matches()) {
                        standardized.append(NameCaseConvention.format(NameCaseConvention.LOWER_CAMEL,method.getSimpleName().toLowerCase()));
                    } else {
                        for (int i = 0; i < name.length; i++) {
                            char c = name[i];

                            if (i == 0) {
                                // the java specification requires identifiers to start with [a-zA-Z$_]
                                if (c != '$' && c != '_') {
                                    standardized.append(Character.toLowerCase(c));
                                }
                            } else {
                                if (!Character.isLetterOrDigit(c)) {
                                    while (i < name.length && (!Character.isLetterOrDigit(name[i]) || name[i] > 'z')) {
                                        i++;
                                    }
                                    if (i < name.length) {
                                        standardized.append(Character.toUpperCase(name[i]));
                                    }
                                } else {
                                    standardized.append(c);
                                }
                            }
                        }
                    }
                    if (!StringUtils.isBlank(standardized.toString())
                            && !methodExists(method.getMethodType(), standardized.toString())) {
                        doNext(new ChangeMethodName(MethodMatcher.methodPattern(method), standardized.toString(), true, false));
                    }
                }

                return super.visitMethodDeclaration(method, executionContext);
            }

            private boolean methodExists(JavaType.Method method, String newName) {
                return TypeUtils.findDeclaredMethod(method.getDeclaringType(), newName, method.getParameterTypes()).orElse(null) != null;
            }
        };
    }
}
