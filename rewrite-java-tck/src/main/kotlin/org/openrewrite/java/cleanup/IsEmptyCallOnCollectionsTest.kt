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

import org.junit.jupiter.api.Test
import org.openrewrite.Issue
import org.openrewrite.Recipe
import org.openrewrite.java.JavaRecipeTest

@Suppress(
    "SizeReplaceableByIsEmpty",
    "DuplicateCondition",
    "ConstantConditions",
    "ExcessiveRangeCheck",
    "ConstantOnWrongSideOfComparison",
    "StatementWithEmptyBody",
    "BooleanMethodNameMustStartWithQuestion",
    "PointlessBooleanExpression",
    "ResultOfMethodCallIgnored",
    "Convert2MethodRef"
)
interface IsEmptyCallOnCollectionsTest : JavaRecipeTest {
    override val recipe: Recipe
        get() = IsEmptyCallOnCollections()

    @Test
    fun isEmptyCallOnCollections() = assertChanged(
        before = """
            import java.util.List;

            class Test {
                static void method(List<String> l) {
                    if (l.size() == 0 || 0 == l.size()) {
                        // empty body
                    } else if (l.size() != 0 || 0 != l.size()) {
                        // empty body
                    }
                }
            }
        """,
        after = """
            import java.util.List;

            class Test {
                static void method(List<String> l) {
                    if (l.isEmpty() || l.isEmpty()) {
                        // empty body
                    } else if (!l.isEmpty() || !l.isEmpty()) {
                        // empty body
                    }
                }
            }
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1112")
    fun formatting() = assertChanged(
        before = """
            import java.util.List;

            class Test {
                static boolean method(List<String> l) {
                    if (true || l.size() == 0) {
                        // empty body
                    }
                    return l.size() == 0;
                }
            }
        """,
        after = """
            import java.util.List;

            class Test {
                static boolean method(List<String> l) {
                    if (true || l.isEmpty()) {
                        // empty body
                    }
                    return l.isEmpty();
                }
            }
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1120")
    fun lambda() = assertChanged(
        before = """
            import java.util.List;
            import java.util.stream.Stream;

            class Test {
                static <T> Stream<List<T>> method(Stream<List<T>> stream) {
                    return stream.filter(s -> s.size() == 0);
                }
            }
        """,
        after = """
            import java.util.List;
            import java.util.stream.Stream;

            class Test {
                static <T> Stream<List<T>> method(Stream<List<T>> stream) {
                    return stream.filter(s -> s.isEmpty());
                }
            }
        """
    )

}
