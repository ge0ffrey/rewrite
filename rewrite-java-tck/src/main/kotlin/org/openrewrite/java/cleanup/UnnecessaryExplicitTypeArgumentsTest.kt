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
package org.openrewrite.java.cleanup

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openrewrite.Issue
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest

@Suppress("RedundantTypeArguments", "InfiniteRecursion", "CodeBlock2Expr")
interface UnnecessaryExplicitTypeArgumentsTest : JavaRecipeTest {
    override val recipe: Recipe
        get() = UnnecessaryExplicitTypeArguments()

    @Test
    fun unnecessaryExplicitTypeArguments(jp: JavaParser) = assertChanged(
        jp,
        before = """
            class Test {
                <T> T test() {
                    String s = this.<String>test();
                    Object o = this.<String>test();
                    return this.<T>test();
                }

                Object o() {
                    return this.<String>test();
                }
            }
        """,
        after = """
            class Test {
                <T> T test() {
                    String s = this.test();
                    Object o = this.<String>test();
                    return this.test();
                }

                Object o() {
                    return this.<String>test();
                }
            }
        """
    )

    @Test
    @Disabled
    fun withinLambda(jp: JavaParser) = assertChanged(
        jp,
        before = """
            import java.util.function.Function;

            class Test {
                Function<Object, Object> f = (d1) -> {
                    return this.<Object>test();
                };

                <T> T test() {
                    return this.test();
                }
            }
        """,
        after = """
            import java.util.function.Function;

            class Test {
                Function<Object, Object> f = (d1) -> {
                    return this.test();
                };

                <T> T test() {
                    return this.test();
                }
            }
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1211")
    @Test
    fun doesNotIntroduceAmbiguity() = assertUnchanged(
        before = """
            import java.util.Collection;

            public class Test {
            
                <G> G foo() {
                    return null;
                }
            
                <E> E fetch(E entity) {
                    return null;
                }
            
                <E> Collection<E> fetch(Collection<E> entity) {
                    return null;
                }
            
                void test() {
                    Integer bar = fetch(this.<Integer>foo());
                }
            }
        """
    )
}
