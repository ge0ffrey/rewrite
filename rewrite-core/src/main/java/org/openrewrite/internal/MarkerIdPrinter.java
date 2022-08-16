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
package org.openrewrite.internal;

import org.openrewrite.ExecutionContext;
import org.openrewrite.PrintOutputCapture;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.marker.RecipesThatMadeChanges;

import java.util.StringJoiner;

public class MarkerIdPrinter extends TreeVisitor<Tree, PrintOutputCapture<ExecutionContext>> {
    @Override
    public Tree visit(@Nullable Tree tree, PrintOutputCapture<ExecutionContext> p) {
        if (tree instanceof Markers && !((Markers) tree).getMarkers().isEmpty()) {
            StringJoiner markerIdJoiner = new StringJoiner(",");
            for (Marker marker : ((Markers) tree).entries()) {
                if (!(marker instanceof RecipesThatMadeChanges)) {
                    markerIdJoiner.add(Integer.toString(marker.hashCode()));
                }
            }
            String markerIds = markerIdJoiner.toString();
            if (!markerIds.isEmpty()) {
                p.out
                        .append("m[")
                        .append(markerIds)
                        .append("]->");
            }
        }
        return super.visit(tree, p);
    }
}

