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
package org.openrewrite.test;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Marker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class SourceSpec<T extends SourceFile> implements SourceSpecs {
    @EqualsAndHashCode.Include
    final UUID id = UUID.randomUUID();

    final Class<T> sourceFileType;

    @Nullable
    final String dsl;

    final ParserSupplier parserSupplier;

    @Nullable
    final String before;

    @Nullable
    final String after;

    /**
     * Apply a function to each SourceFile after recipe execution.
     * Useful for validating the AST or its metadata.
     */
    final EachResult eachResult;

    public interface EachResult {
        EachResult noop = (sourceFile, testMethodSpec, testClassSpec) -> sourceFile;
        SourceFile accept(SourceFile sourceFile, RecipeSpec testMethodSpec, RecipeSpec testClassSpec);
    }

    final Consumer<ExecutionContext> customizeExecutionContext;

    public SourceSpec(Class<T> sourceFileType, @Nullable String dsl,
                      ParserSupplier parserSupplier, @Nullable String before, @Nullable String after) {
        this.sourceFileType = sourceFileType;
        this.dsl = dsl;
        this.parserSupplier = parserSupplier;
        this.before = before;
        this.after = after;
        this.eachResult = EachResult.noop;
        this.customizeExecutionContext = (ctx) -> {};
    }

    @Setter
    @Nullable
    protected String sourceSetName;

    protected Path dir = Paths.get("");

    @Nullable
    protected Path sourcePath;

    protected final List<Marker> markers = new ArrayList<>();

    @Nullable
    Path getSourcePath() {
        return sourcePath == null ? null : dir.resolve(sourcePath);
    }

    protected Consumer<T> beforeRecipe = t -> {

    };

    protected Consumer<T> afterRecipe = t -> {
    };

    public SourceSpec<T> path(Path sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public SourceSpec<T> path(String sourcePath) {
        this.sourcePath = Paths.get(sourcePath);
        return this;
    }

    public SourceSpec<T> markers(Marker... markers) {
        Collections.addAll(this.markers, markers);
        return this;
    }

    public SourceSpec<T> beforeRecipe(Consumer<T> beforeRecipe) {
        this.beforeRecipe = beforeRecipe;
        return this;
    }

    public SourceSpec<T> afterRecipe(Consumer<T> afterRecipe) {
        this.afterRecipe = afterRecipe;
        return this;
    }

    @Override
    public Iterator<SourceSpec<?>> iterator() {
        return new Iterator<SourceSpec<?>>() {
            boolean next = true;

            @Override
            public boolean hasNext() {
                return next;
            }

            @Override
            public SourceSpec<?> next() {
                next = false;
                return SourceSpec.this;
            }
        };
    }
}
