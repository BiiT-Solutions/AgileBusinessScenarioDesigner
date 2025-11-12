package com.biit.abcd.persistence.entity.diagram;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

public enum DiagramObjectType {

    SOURCE("biit.SourceNode", "source.svg"),
    SINK("biit.SinkNode", "sink.svg"),
    FORK("biit.ForkNode", "fork.svg"),
    DIAGRAM_CHILD("biit.Diagram", "diagram.svg"),
    RULE("biit.RuleNode", "rule.svg"),
    TABLE("biit.TableNode", "table.svg"),
    CALCULATION("biit.CalculationNode", "expression.svg"),
    REPEAT("biit.BaseRepeatNode", ""),
    LINK("link", "");

    private String jsonTypeName;
    private String graphvizIcon;

    DiagramObjectType(String jsonTypeName, String graphvizIcon) {
        this.jsonTypeName = jsonTypeName;
        this.graphvizIcon = graphvizIcon;
    }

    public String getJsonType() {
        return jsonTypeName;
    }

    public static DiagramObjectType get(String name) {
        for (DiagramObjectType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public static DiagramObjectType getByJsonType(String jsonType) {
        for (DiagramObjectType value : values()) {
            if (value.jsonTypeName.equalsIgnoreCase(jsonType)) {
                return value;
            }
        }
        return null;
    }

    public String getGraphvizIcon() {
        return graphvizIcon;
    }
}
