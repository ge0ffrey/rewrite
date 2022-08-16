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

class ChangeDependencyClassifierTest : RewriteTest {
    @Test
    fun worksWithEmptyStringConfig() = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier("org.openrewrite", "*", "classified", ""))
        },
        buildGradle("""
            dependencies {
                api 'org.openrewrite:rewrite-gradle:latest.release:classifier'
            }
        """,
        """
            dependencies {
                api 'org.openrewrite:rewrite-gradle:latest.release:classified'
            }
        """)
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun findDependency(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier(group, artifact, "classified", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release:classifier'
                    api "org.openrewrite:rewrite-core:latest.release:classifier"
                }
            """,
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release:classified'
                    api "org.openrewrite:rewrite-core:latest.release:classified"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun findMapStyleDependency(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier(group, artifact, "classified", null))
        },
        buildGradle(
            """
                dependencies {
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier"
                }
            """,
            """
                dependencies {
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classified'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classified"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithoutVersion(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier(group, artifact, "classified", null))
        },
        buildGradle(
            """
                dependencies {
                    api group: 'org.openrewrite', name: 'rewrite-core', classifier: 'classifier'
                    api group: "org.openrewrite", name: "rewrite-core", classifier: "classifier"
                }
            """,
            """
                dependencies {
                    api group: 'org.openrewrite', name: 'rewrite-core', classifier: 'classified'
                    api group: "org.openrewrite", name: "rewrite-core", classifier: "classified"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithClassifier(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier(group, artifact, "classified", null))
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
                    api 'org.openrewrite:rewrite-core:latest.release:classified'
                    api "org.openrewrite:rewrite-core:latest.release:classified"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classified'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classified"
                }
            """
        )
    )

    @ParameterizedTest
    @CsvSource(value = ["org.openrewrite:rewrite-core", "*:*"], delimiterString = ":")
    fun worksWithExt(group: String, artifact: String) = rewriteRun(
        { spec ->
            spec.recipe(ChangeDependencyClassifier(group, artifact, "classified", null))
        },
        buildGradle(
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release:classifier@ext'
                    api "org.openrewrite:rewrite-core:latest.release:classifier@ext"
                    api group: 'org.openrewrite', name: 'rewrite-core', classifier: 'classifier'
                    api group: "org.openrewrite", name: "rewrite-core", classifier: "classifier"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classifier', extension: 'ext'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classifier", extension: "ext"
                }
            """,
            """
                dependencies {
                    api 'org.openrewrite:rewrite-core:latest.release:classified@ext'
                    api "org.openrewrite:rewrite-core:latest.release:classified@ext"
                    api group: 'org.openrewrite', name: 'rewrite-core', classifier: 'classified'
                    api group: "org.openrewrite", name: "rewrite-core", classifier: "classified"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classified'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classified"
                    api group: 'org.openrewrite', name: 'rewrite-core', version: 'latest.release', classifier: 'classified', extension: 'ext'
                    api group: "org.openrewrite", name: "rewrite-core", version: "latest.release", classifier: "classified", extension: "ext"
                }
            """
        )
    )
}
