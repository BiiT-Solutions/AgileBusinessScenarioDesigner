package com.biit.abcd.serialization;

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

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class CustomVariableSerializer extends StorableObjectSerializer<CustomVariable> {

    @Override
    public void serialize(CustomVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getName() != null) {
            jgen.writeStringField("name", src.getName());
        }
        if (src.getScope() != null) {
            jgen.writeStringField("scope", src.getScope().name());
        }
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getDefaultValue() != null) {
            jgen.writeStringField("defaultValue", src.getDefaultValue());
        }
    }
}
