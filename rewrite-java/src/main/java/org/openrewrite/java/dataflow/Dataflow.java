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
package org.openrewrite.java.dataflow;

import lombok.RequiredArgsConstructor;
import org.openrewrite.Cursor;
import org.openrewrite.Incubating;
import org.openrewrite.java.controlflow.ControlFlow;
import org.openrewrite.java.dataflow.analysis.ForwardFlow;
import org.openrewrite.java.dataflow.analysis.SinkFlow;
import org.openrewrite.java.dataflow.analysis.SourceFlow;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;

import java.util.Optional;
import java.util.Set;

/**
 * <a href="https://en.wikipedia.org/wiki/Dataflow_programming">Dataflow</a>.
 */
@Incubating(since = "7.24.0")
@RequiredArgsConstructor(staticName = "startingAt")
public class Dataflow {
    private final Cursor start;

    public <Source extends Expression, Sink extends J> Optional<SinkFlow<Source, Sink>> findSinks(LocalFlowSpec<Source, Sink> spec) {
        Object value = start.getValue();
        if (spec.getSourceType().isAssignableFrom(value.getClass())) {
            //noinspection unchecked
            Source source = (Source) value;
            if (!spec.isSource(source, start)) {
                return Optional.empty();
            }
            return ControlFlow.startingAt(start).findControlFlow().flatMap(summary -> {
                Set<Expression> reachable = summary.computeReachableExpressions(spec::isBarrierGuard);

                SinkFlow<Source, Sink> flow = new SinkFlow<>(start, spec, reachable);

                ForwardFlow.findSinks(flow);
                return flow.isNotEmpty() ? Optional.of(flow) : Optional.empty();
            });
        }
        return Optional.empty();
    }

    public <E extends Expression> SourceFlow<E> findSources(LocalFlowSpec<E, ?> spec) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
