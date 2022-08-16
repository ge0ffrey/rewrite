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
@file:Suppress("NullableProblems")

package org.openrewrite.java.cleanup

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest

interface UnnecessaryPrimitiveAnnotationsTest : JavaRecipeTest {
    override val recipe: Recipe
        get() = UnnecessaryPrimitiveAnnotations()

    override val parser: JavaParser
        get() = JavaParser.fromJavaVersion().classpath("jsr305").build()

    @Test
    fun nullableOnNonPrimitive() = assertUnchanged(
        before = """
            import javax.annotation.CheckForNull;
            import javax.annotation.Nullable;
            class A {
                @Nullable
                private long[] partitionLengths;
                
                @CheckForNull
                public Object getCount(@Nullable Object val) {
                    return val;
                }
                
                @Nullable
                public byte[] getBytes() {
                    return null;
                }
                
                public void doSomething(long requestId, long stageId, String component, String host,
                                          String type, boolean skipFailure) {
                }
            }
        """
    )

    @Test
    fun unnecessaryNullable() = assertChanged(
        before = """
            import javax.annotation.CheckForNull;
            import javax.annotation.Nullable;
            class A {
                @CheckForNull
                public int getCount(@Nullable int val) {
                    return val;
                }
            }
        """,
        after = """
            class A {
    
                public int getCount(int val) {
                    return val;
                }
            }
        """
    )
}