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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.openrewrite.ExecutionContext
import org.openrewrite.Issue
import org.openrewrite.Recipe
import org.openrewrite.java.Assertions
import org.openrewrite.java.Assertions.java
import org.openrewrite.java.JavaIsoVisitor
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest
import org.openrewrite.test.TypeValidation
import org.openrewrite.java.tree.J
import org.openrewrite.test.RecipeSpec
import org.openrewrite.test.RewriteTest

@Issue("https://github.com/openrewrite/rewrite/issues/466")
interface MethodNameCasingTest: JavaRecipeTest, RewriteTest {
    override val recipe: Recipe?
        get() = MethodNameCasing(false)

    override val parser: JavaParser
        get() {
            val jp = JavaParser.fromJavaVersion().build()
            jp.setSourceSet("main")
            return jp
        }

    val testParser: JavaParser
        get() {
            val jp = JavaParser.fromJavaVersion().build()
            jp.setSourceSet("test")
            return jp
        }

    override fun defaults(spec: RecipeSpec) {
        spec.recipe(MethodNameCasing(true))
    }

    @Issue("https://github.com/openrewrite/rewrite/issues/1741")
    @Test
    fun doNotApplyToTest() = assertUnchanged(
        testParser,
        before = """
            class Test {
                void MyMethod_with_über() {
                }
            }
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1741")
    @Test
    fun applyChangeToTest() = assertChanged(
        testParser,
        recipe = MethodNameCasing(true),
        before = """
            class Test {
                void MyMethod_with_über() {
                }
            }
        """,
        after = """
            class Test {
                void myMethodWithBer() {
                }
            }
        """
    )

    @Test
    fun changeMethodDeclaration() = assertChanged(
        before = """
            class Test {
                void MyMethod_with_über() {
                }
            }
        """,
        after = """
            class Test {
                void myMethodWithBer() {
                }
            }
        """
    )

    @Test
    fun changeCamelCaseMethodWithFirstLetterUpperCase() = assertChanged(
        before = """
            class Test {
                void MyMethod() {
                }
            }
        """,
        after = """
            class Test {
                void myMethod() {
                }
            }
        """
    )

    @Test
    fun changeMethodInvocations() = assertChanged(
        dependsOn = arrayOf("""
            class Test {
                void MyMethod_with_über() {
                }
            }
        """),
        before = """
            class A {
                void test() {
                    new Test().MyMethod_with_über();
                }
            }
        """,
        after = """
            class A {
                void test() {
                    new Test().myMethodWithBer();
                }
            }
        """
    )

    @Test
    fun dontChangeCorrectlyCasedMethods() = assertUnchanged(
        before = """
            class Test {
                void dontChange() {
                }
            }
        """
    )

    @Test
    fun changeMethodNameWhenOverride() = assertChanged(
        dependsOn = arrayOf(
            """
            class ParentClass {
                void _method() {
                }
            }
        """
        ),
        before = """
            class Test extends ParentClass {
                @Override
                void _method() {
                }
            }
        """,
        after = """
            class Test extends ParentClass {
                @Override
                void method() {
                }
            }
        """
    )

    @Test
    fun newNameExists() = assertUnchanged(
        before = """
            class Test {
                void _method() {
                }
                void method() {
                }
            }
        """
    )

    @Test
    fun nameExistsInInnerClass() = assertChanged(
        before = """
            class T {
                public void _method(){}
                
                private static class M {
                    public void _method(){}
                }
            }
        """,
        after = """
            class T {
                public void method(){}
                
                private static class M {
                    public void method(){}
                }
            }
        """
    )

    @Suppress("UnusedAssignment")
    @Issue("https://github.com/openrewrite/rewrite/issues/2103")
    @Test
    fun `rename snake_case to camelCase`() = assertChanged(
        before = """
            class T {
                private static int SOME_METHOD() {
                  return 1;
                }
                private static int some_method_2() {
                  return 1;
                }
                private static int some_über_method() {
                  return 1;
                }
                public static void anotherMethod() {
                  int i = SOME_METHOD();
                  i = some_method_2();
                  i = some_über_method();
                }
            }
        """,
        after = """
            class T {
                private static int someMethod() {
                  return 1;
                }
                private static int someMethod2() {
                  return 1;
                }
                private static int someBerMethod() {
                  return 1;
                }
                public static void anotherMethod() {
                  int i = someMethod();
                  i = someMethod2();
                  i = someBerMethod();
                }
            }
        """
    )

    // This test uses a recipe remove ClassDeclaration types information prior to running the MethodNameCasing recipe.
    // This results in a change with an empty diff, thus before and after sources are identical
    @Issue("https://github.com/openrewrite/rewrite/issues/2103")
    @Test
    fun `does not rename method invocations when the method declarations class type is null`() = rewriteRun(
        { spec ->
            spec.typeValidationOptions(TypeValidation.none()).recipe(
                RewriteTest.toRecipe {
                    object : JavaIsoVisitor<ExecutionContext>() {
                        override fun visitClassDeclaration(classDecl: J.ClassDeclaration, p: ExecutionContext): J.ClassDeclaration {
                            return super.visitClassDeclaration(classDecl, p).withType(null)
                        }
                    }
                }.doNext(MethodNameCasing(true))
            )
        },
        java(
            """
            package abc;
            class T {
                public static int MyMethod() {return null;}
                public static void anotherMethod() {
                    int i = MyMethod();
                }
            }
            """,
            """
            package abc;
            class T {
                public static int MyMethod() {return null;}
                public static void anotherMethod() {
                    int i = MyMethod();
                }
            }
            """
        )
    )

    @Test
    fun `keep camel case when removing leading underscore`() = assertChanged(
        before = """
            class Test {
                private void _theMethod() {
                
                }
            }
        """,
        after = """
            class Test {
                private void theMethod() {
                
                }
            }
        """
    )

    @Test
    fun `keep camel case when removing leading underscore 2`() = assertChanged(
        before = """
            import java.util.*;
            
            class Test {
                private List<String> _getNames() {
                    List<String> result = new ArrayList<>();
                    result.add("Alice");
                    result.add("Bob");
                    result.add("Carol");
                    return result;
                }
                
                public void run() {
                    for (String n: _getNames()) {
                        System.out.println(n);
                    }
                }
            }
        """,
        after = """
            import java.util.*;
            
            class Test {
                private List<String> getNames() {
                    List<String> result = new ArrayList<>();
                    result.add("Alice");
                    result.add("Bob");
                    result.add("Carol");
                    return result;
                }
                
                public void run() {
                    for (String n: getNames()) {
                        System.out.println(n);
                    }
                }
            }
        """
    )
    @Test
    fun `change name of method with array argument`() = assertChanged(
        before = """
            import java.util.*;
            
            class Test {
                private List<String> _getNames(String[] names) {
                    List<String> result = new ArrayList<>(Arrays.asList(names));
                    return result;
                }
            }
        """,
        after = """
            import java.util.*;
            
            class Test {
                private List<String> getNames(String[] names) {
                    List<String> result = new ArrayList<>(Arrays.asList(names));
                    return result;
                }
            }
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/2261")
    @Test
    fun unknownParameterTypes() = rewriteRun(
        { spec ->
            spec.typeValidationOptions(TypeValidation.none())
        },
        java("""
            class Test {
                private void _foo(Unknown u) {
                }
            }
        """,
        """
            class Test {
                private void foo(Unknown u) {
                }
            }
        """)
    )
}
