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

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openrewrite.groovy.Assertions.groovy
import org.openrewrite.test.RewriteTest

class ForLoopTest : RewriteTest {
    @Suppress("GrUnnecessarySemicolon")
    @Test
    fun forLoopMultipleInit() = rewriteRun(
        groovy(
            // IntelliJ's Groovy support is confused by multiple assignment in a for loop,
            // but Groovy itself does support this construct:
            //    https://groovy-lang.org/semantics.html#_enhanced_classic_java_style_for_loop
            """
                int i
                int j
                for(i = 0, j = 0;;) {
                }
            """
        )
    )

    @Test
    fun forLoopMultipleUpdate() = rewriteRun(
        groovy(
            """
               int i = 0
               int j = 10
               for(; i < j; i++ , j-- ) { }
            """
        )
    )

    @Test
    fun forLoop() = rewriteRun(
        groovy(
            """
                for(int i = 0; i < 10; i++) {
                }
            """
        )
    )

    @Test
    fun infiniteLoop() = rewriteRun(
        groovy(
            """
                for(;;) {
                }
            """
        )
    )

    @Test
    fun format() = rewriteRun(
        groovy(
            """
                for ( int i = 0 ; i < 10 ; i++ ) {
                }
            """
        )
    )

    @Test
    fun formatInfiniteLoop() = rewriteRun(
        groovy(
            """
                for ( ; ; ) {}
            """
        )
    )

    @Test
    fun formatLoopNoInit() = rewriteRun(
        groovy(
            """
                for ( ; i < 10 ; i++ ) {}
            """
        )
    )

    @Test
    fun formatLoopNoCondition() = rewriteRun(
        groovy(
            """
                int i = 0;
                for(; i < 10; i++) {}
            """
        )
    )

    @Test
    fun statementTerminatorForSingleLineForLoops() = rewriteRun(
        groovy(
            """
                for(;;) test()
            """
        )
    )

    @Test
    fun initializerIsAnAssignment() = rewriteRun(
        groovy(
            """
                def a = [1,2]
                int i=0
                for(i=0; i<a.length; i++) {}
            """
        )
    )

    @Disabled
    @Test
    fun multiVariableInitialization() = rewriteRun(
        groovy(
            """
                for(int i, j = 0;;) {}
            """
        )
    )

    @Test
    fun forEachWithColon() = rewriteRun(
        groovy(
            """
                for(int i : [1, 2, 3]) {}
            """
        )
    )

    @Test
    fun forEachWithIn() = rewriteRun(
        groovy(
            """
                for(int i in [1, 2, 3]) {}
            """
        )
    )
}
