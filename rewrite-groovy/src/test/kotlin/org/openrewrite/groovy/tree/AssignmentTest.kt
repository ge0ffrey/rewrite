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
package org.openrewrite.groovy.tree

import org.junit.jupiter.api.Test
import org.openrewrite.Issue
import org.openrewrite.groovy.Assertions.groovy
import org.openrewrite.test.RewriteTest

@Suppress("GroovyUnusedAssignment", "GrUnnecessarySemicolon")
class AssignmentTest : RewriteTest {

    @Test
    fun concat() = rewriteRun(
        groovy(
            """
                android {
                    // specify the artifactId as module-name for kotlin
                    kotlinOptions.freeCompilerArgs += ["-module-name", POM_ARTIFACT_ID]
                }
            """
        )
    )

    @Test
    fun assignment() = rewriteRun(
        groovy(
            """
                String s;
                s = "foo";
            """
        )
    )

    @Test
    fun unaryMinus() = rewriteRun(
        groovy(
            """
                def i = -1
                def l = -1L
                def f = -1.0f
                def d = -1.0d
            """
        )
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1522")
    @Test
    fun unaryPlus() = rewriteRun(
        groovy(
            """
                int k = +10
            """
        )
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1533")
    @Test
    fun baseNConversions() = rewriteRun(
        groovy(
            """
                def a = 01
                def b = 001
                def c = 0001
                def d = 00001
                def e = 000001
            """
        )
    )
}
