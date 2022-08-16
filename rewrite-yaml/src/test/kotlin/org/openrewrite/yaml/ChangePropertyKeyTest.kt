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
package org.openrewrite.yaml

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.openrewrite.Issue
import org.openrewrite.Recipe
import java.nio.file.Path

class ChangePropertyKeyTest : YamlRecipeTest {
    override val recipe: Recipe
        get() = ChangePropertyKey(
            "management.metrics.binders.*.enabled",
            "management.metrics.enable.process.files",
            null,
            null,
            null
        )

    @Issue("https://github.com/openrewrite/rewrite/issues/1873")
    @Test
    fun `shorter new key with indented config`() = assertChanged(
        recipe = ChangePropertyKey("a.b.c.d.e", "x.y", null, null, null),
        before =
        """
        a:
          b:
            c:
              d:
                e:
                  child: true
        """,
            after = """
        x.y:
          child: true
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1873")
    @Test
    fun `longer new key with indented config`() = assertChanged(
        recipe = ChangePropertyKey("x.y", "a.b.c.d.e",  null, null, null),
        before =
        """
        x:
          y:
            child: true
        """,
            after = """
        a.b.c.d.e:
          child: true
        """
    )

    @Test
    fun singleEntry() = assertChanged(
        before = "management.metrics.binders.files.enabled: true",
        after = "management.metrics.enable.process.files: true"
    )

    @Test
    fun singleGlobEntry() = assertChanged(
        before = "management.metrics.binders.files.enabled: true",
        after = "management.metrics.enable.process.files: true"
    )

    @Test
    fun nestedEntry() = assertChanged(
        recipe = ChangePropertyKey(
            "management.metrics.binders.files.enabled",
            "management.metrics.enable.process.files",
            null,
            null,
            null
        ),
        before = """
            unrelated.property: true
            management.metrics:
                binders:
                    jvm.enabled: true
                    files.enabled: true
        """,
        after = """
            unrelated.property: true
            management.metrics:
                binders:
                    jvm.enabled: true
                enable.process.files: true
        """
    )

    @Test
    fun nestedEntryEmptyPartialPathRemoved() = assertChanged(
        before = """
            unrelated.property: true
            management.metrics:
                binders:
                    files.enabled: true
        """,
        after = """
            unrelated.property: true
            management.metrics:
                enable.process.files: true
        """
    )

    @Nested
    inner class AvoidsRegenerativeChanges {
        @Test
        fun `indented property`() = assertUnchanged(
            recipe = ChangePropertyKey("a.b.c", "a.b.c.d", null, null, null),
            before = """
            a:
              b:
                c:
                  d: true
            """
        )

        @Test
        fun `dot-separated property equal to newPropertyKey`() = assertUnchanged(
            recipe = ChangePropertyKey("a.b.c", "a.b.c.d", null, null, null),
            before = "a.b.c.d: true",
        )

        @Test
        fun `dot-separated property including newPropertyKey`() = assertUnchanged(
            recipe = ChangePropertyKey("a.b.c", "a.b.c.d", null, null, null),
            before = "a.b.c.d.x: true",
        )
    }

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1114")
    fun `change path to one path longer`() = assertChanged(
        recipe = ChangePropertyKey("a.b.c", "a.b.c.d", null, null, null),
        before = "a.b.c: true",
        after = "a.b.c.d: true",
    )

    @Test
    fun `change path to one path shorter`() = assertChanged(
        recipe = ChangePropertyKey("a.b.c.d", "a.b.c", null, null, null),
        before = "a.b.c.d: true",
        after = "a.b.c: true"
    )

    @Test
    fun changeOnlyMatchingFile(@TempDir tempDir: Path) {
        val matchingFile = tempDir.resolve("a.yml").apply {
            toFile().parentFile.mkdirs()
            toFile().writeText("management.metrics.binders.files.enabled: true")
        }.toFile()
        val nonMatchingFile = tempDir.resolve("b.yml").apply {
            toFile().parentFile.mkdirs()
            toFile().writeText("management.metrics.binders.files.enabled: true")
        }.toFile()
        val recipe = ChangePropertyKey(
            "management.metrics.binders.files.enabled",
            "management.metrics.enable.process.files",
            null,
            "**/a.yml",
            null
        )
        assertChanged(recipe = recipe, before = matchingFile, after = "management.metrics.enable.process.files: true")
        assertUnchanged(recipe = recipe, before = nonMatchingFile)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "acme.my-project.person.first-name",
            "acme.myProject.person.firstName",
            "acme.my_project.person.first_name",
        ]
    )
    @Issue("https://github.com/openrewrite/rewrite/issues/1168")
    fun relaxedBinding(propertyKey: String) = assertChanged(
        recipe = ChangePropertyKey(propertyKey, "acme.my-project.person.changed-first-name-key", true, null, null),
        before = """
            unrelated.root: true
            acme.my-project:
                unrelated: true
                person:
                    unrelated: true
                    first-name: example
        """,
        after = """
            unrelated.root: true
            acme.my-project:
                unrelated: true
                person:
                    unrelated: true
                    changed-first-name-key: example
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1168")
    fun exactMatch() = assertChanged(
        recipe = ChangePropertyKey(
            "acme.my-project.person.first-name",
            "acme.my-project.person.changed-first-name-key",
            false,
            null,
            null
        ),
        before = """
            acme.myProject.person.firstName: example
            acme.my_project.person.first_name: example
            acme.my-project.person.first-name: example
        """,
        after = """
            acme.myProject.person.firstName: example
            acme.my_project.person.first_name: example
            acme.my-project.person.changed-first-name-key: example
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1249")
    @Test
    fun updateKeyAndDoesNotMergeToSibling() = assertChanged(
        recipe = ChangePropertyKey(
            "i",
            "a.b.c",
            false,
            null,
            null
        ),
        before = """
            a:
              b:
                f0: v0
                f1: v1
            i:
              f0: v0
              f1: v1
        """,
        after = """
            a:
              b:
                f0: v0
                f1: v1
            a.b.c:
              f0: v0
              f1: v1
        """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1249")
    @Test
    fun updateKeyAndDoesNotMergeToSiblingWithCoalescedProperty() = assertChanged(
        recipe = ChangePropertyKey(
            "old-property",
            "new-property.sub-property.super-sub",
            true,
            null,
            null
        ),
        before = """
            newProperty.subProperty:
                superSub:
                  f0: v0
                  f1: v1
            oldProperty:
              f0: v0
              f1: v1
        """,
        after = """
            newProperty.subProperty:
                superSub:
                  f0: v0
                  f1: v1
            new-property.sub-property.super-sub:
              f0: v0
              f1: v1
        """
    )

    @Test
    fun doesNotChangeKeyWithSequenceInPath() = assertUnchanged(
        recipe = ChangePropertyKey(
            "a.b.c.a0",
            "a.b.a0",
            true,
            null,
            null
        ),
        before = """
            a:
              b:
                c:
                  - a0: x
                    a1: 'y'
                  - aa1: x
                    a1: 'y'
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/434")
    fun doesNotChangePropertyOrdering() = assertChanged(
        recipe = ChangePropertyKey(
            "description",
            "newDescription",
            false,
            null,
            null
        ),
        before = """
        id: something
        description: desc
        other: whatever
    """,
        after = """
        id: something
        newDescription: desc
        other: whatever
    """
    )

    @Issue("https://github.com/openrewrite/rewrite/issues/1744")
    @Test
    fun updatePropertyWithMapping() = assertChanged(
        recipe = ChangePropertyKey("app.foo.change.from", "app.bar.change.to", null, null, null),
        before = """
            app:
              foo.change.from: hi
              bar:
                other:
                  property: bye
        """,
        after = """
            app:
              bar.change.to: hi
              bar:
                other:
                  property: bye
        """
    )

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Test
    fun checkValidation() {
        var recipe = ChangePropertyKey(null, null, null, null, null)
        var valid = recipe.validate()
        assertThat(valid.isValid).isFalse
        assertThat(valid.failures()).hasSize(2)
        assertThat(valid.failures()[0].property).isEqualTo("newPropertyKey")
        assertThat(valid.failures()[1].property).isEqualTo("oldPropertyKey")

        recipe = ChangePropertyKey(null, "management.metrics.enable.process.files", null, null, null)
        valid = recipe.validate()
        assertThat(valid.isValid).isFalse
        assertThat(valid.failures()).hasSize(1)
        assertThat(valid.failures()[0].property).isEqualTo("oldPropertyKey")

        recipe = ChangePropertyKey("management.metrics.binders.files.enabled", null, null, null, null)
        valid = recipe.validate()
        assertThat(valid.isValid).isFalse
        assertThat(valid.failures()).hasSize(1)
        assertThat(valid.failures()[0].property).isEqualTo("newPropertyKey")

        recipe =
            ChangePropertyKey(
                "management.metrics.binders.files.enabled",
                "management.metrics.enable.process.files",
                null,
                null,
                null
            )
        valid = recipe.validate()
        assertThat(valid.isValid).isTrue
    }

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1841")
    fun doesNotReformatUnrelatedProperties() = assertChanged(
        before = """
            unrelated:
              property: true
            management.metrics:
              binders.files.enabled: true
            other:
              property: true
        """,
        after = """
            unrelated:
              property: true
            management.metrics:
              enable.process.files: true
            other:
              property: true
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/1841")
    fun relocatesPropertyIfNothingElseInFamily() = assertChanged(
        recipe = ChangePropertyKey("a.b.c", "x.y.z", true, null, null),
        before = """
            a:
              b:
                c: abc
            something:
              else: qwe
        """,
        after = """
            something:
              else: qwe
            x.y.z: abc
        """
    )

    @Test
    @Issue("https://github.com/openrewrite/rewrite/issues/2016")
    fun relocatesPropertyWithSamePrefix() = assertChanged(
        recipe = ChangePropertyKey(
            "spring.elasticsearch.rest.sniffer.interval",
            "spring.elasticsearch.restclient.sniffer.interval",
            true,
            null,
            null
        ),
        before = """
            spring:
              elasticsearch:
                rest:
                  sniffer:
                    interval: 1
        """,
        after = """
            spring:
              elasticsearch:
                  restclient.sniffer.interval: 1
        """
    )

    @Nested
    @Issue("https://github.com/openrewrite/rewrite-spring/issues/189")
    inner class WhenOldPropertyKeyIsPrefixOfDotSeparatedKey {
        @Test
        fun `scalar value`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, null),
            before = """
                spring.profiles.group.prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring.config.activate.on-profile.group.prod: proddb,prodmq,prodmetrics
            """
        )

        @Test
        fun `mapping value`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, null),
            before = """
                spring.profiles.group:
                  prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring.config.activate.on-profile.group:
                  prod: proddb,prodmq,prodmetrics
            """
        )

        @Test
        fun `match split across parent entries`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, null),
            before = """
                spring:
                  profiles.group:
                    prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring:
                  config.activate.on-profile.group:
                    prod: proddb,prodmq,prodmetrics
            """
        )
    }

    @Nested
    @Issue("https://github.com/openrewrite/rewrite-spring/issues/189")
    inner class Except {
        @Nested
        inner class DotAndIndentCombinations {
            @Test
            fun `dot dot dot`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring.profiles.group.prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `dot dot indent`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring.profiles.group:
                      prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `dot indent dot`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring.profiles:
                      group.prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `dot indent indent`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring.profiles:
                      group:
                        prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `indent dot dot`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring:
                      profiles.group.prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `indent dot indent`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring:
                      profiles.group:
                        prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `indent indent dot`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring:
                      profiles:
                        group.prod: proddb,prodmq,prodmetrics
                """
            )

            @Test
            fun `indent indent indent`() = assertUnchanged(
                recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
                before = """
                    spring:
                      profiles:
                        group:
                          prod: proddb,prodmq,prodmetrics
                """
            )
        }

        @Test
        fun `multiple excluded entries`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group", "active", "include")),
            before = """
                spring:
                  profiles:
                    active: allEnvs
                    include: baseProfile
                    foo: bar
                    group:
                      prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring:
                  profiles:
                    active: allEnvs
                    include: baseProfile
                    group:
                      prod: proddb,prodmq,prodmetrics
                  config.activate.on-profile:
                    foo: bar
            """
        )

        @Test
        fun `target mapping includes non-excluded entry with scalar value`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
            before = """
                spring:
                  profiles:
                    foo: bar
                    group:
                      prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring:
                  profiles:
                    group:
                      prod: proddb,prodmq,prodmetrics
                  config.activate.on-profile:
                    foo: bar
            """
        )

        @Test
        fun `target mapping includes non-excluded entry with mapping value`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
            before = """
                spring:
                  profiles:
                    foo:
                      bar: qwe
                    group:
                      prod: proddb,prodmq,prodmetrics
            """,
            after = """
                spring:
                  profiles:
                    group:
                      prod: proddb,prodmq,prodmetrics
                  config.activate.on-profile:
                    foo:
                      bar: qwe
            """
        )

        @Test
        fun `target mapping has scalar value`() = assertChanged(
            recipe = ChangePropertyKey("spring.profiles", "spring.config.activate.on-profile", null, null, listOf("group")),
            before = """
                spring:
                  profiles: foo
            """,
            after = """
                spring:
                  config.activate.on-profile: foo
            """
        )
    }
}
