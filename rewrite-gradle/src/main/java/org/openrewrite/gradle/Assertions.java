/*
 * Copyright 2022 the original author or authors.
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
package org.openrewrite.gradle;

import org.intellij.lang.annotations.Language;
import org.openrewrite.groovy.GroovyParser;
import org.openrewrite.groovy.tree.G;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.test.ParserSupplier;
import org.openrewrite.test.SourceSpec;
import org.openrewrite.test.SourceSpecs;

import java.nio.file.Paths;
import java.util.function.Consumer;

public class Assertions {

    private Assertions() {
    }

    static final ParserSupplier parserSupplier = new ParserSupplier(G.CompilationUnit.class, "gradle", () ->
            new GradleParser(GroovyParser.builder()
                    .logCompilationWarningsAndErrors(true)));

    public static SourceSpecs buildGradle(@Language("groovy") @Nullable String before) {
        return buildGradle(before, s -> {
        });
    }

    public static SourceSpecs buildGradle(@Language("groovy") @Nullable String before, Consumer<SourceSpec<G.CompilationUnit>> spec) {
        SourceSpec<G.CompilationUnit> gradle = new SourceSpec<>(G.CompilationUnit.class, "gradle", parserSupplier, before, null);
        gradle.path(Paths.get("build.gradle"));
        spec.accept(gradle);
        return gradle;
    }

    public static SourceSpecs buildGradle(@Language("groovy") @Nullable String before, @Language("groovy") String after) {
        return buildGradle(before, after, s -> {
        });
    }

    public static SourceSpecs buildGradle(@Language("groovy") @Nullable String before, @Language("groovy") String after,
                                    Consumer<SourceSpec<G.CompilationUnit>> spec) {
        SourceSpec<G.CompilationUnit> gradle = new SourceSpec<>(G.CompilationUnit.class, "gradle", parserSupplier, before, after);
        gradle.path("build.gradle");
        spec.accept(gradle);
        return gradle;
    }
}
