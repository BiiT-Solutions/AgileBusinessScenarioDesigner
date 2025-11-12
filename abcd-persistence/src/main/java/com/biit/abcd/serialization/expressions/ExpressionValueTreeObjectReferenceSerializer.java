package com.biit.abcd.serialization.expressions;

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

import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueTreeObjectReferenceSerializer extends ExpressionValueSerializer<ExpressionValueTreeObjectReference> {

    @Override
    public void serialize(ExpressionValueTreeObjectReference src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getReference() != null) {
            jgen.writeStringField("referenceId", String.valueOf(src.getReference().getComparationId()));
        }
        if (src.getUnit() != null) {
            jgen.writeStringField("unit", src.getUnit().name());
        }
    }
}
