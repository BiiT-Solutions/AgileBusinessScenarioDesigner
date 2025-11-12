package com.biit.abcd.serialization.diagram;

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

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramTableSerializer extends DiagramElementSerializer<DiagramTable> {

    private static final String DEFAULT_NODE_NAME = "Table";

    @Override
    public void serialize(DiagramTable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        // Set the current text value.
        if (src.getTable() != null) {
            src.getText().setText(src.getTable().getName());
        } else {
            src.getText().setText(DEFAULT_NODE_NAME);
        }
        if (src.getTable() != null) {
            jgen.writeStringField("tableId", src.getTable().getComparationId());
        }
    }
}
