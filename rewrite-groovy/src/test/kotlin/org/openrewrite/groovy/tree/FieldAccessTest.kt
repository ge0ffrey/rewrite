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
import org.openrewrite.groovy.Assertions.groovy
import org.openrewrite.test.RewriteTest

class FieldAccessTest : RewriteTest {

    @Test
    fun starAccess() = rewriteRun(
        groovy(
            """
                all*.exclude group: 'com.netflix.archaius', module: 'archaius-core'
            """
        )
    )

    @Test
    fun fieldAccess() = rewriteRun(
        groovy(
            """
                class Test {
                    public Test field = new Test()
                    Test b = new Test() . field . field
                }
            """
        )
    )

    @Test
    fun nullSafeDereference() = rewriteRun(
        groovy(
            """
                class Test {
                    Integer n
                }
                Test t = new Test()
                t?.n
            """
        )
    )
}
