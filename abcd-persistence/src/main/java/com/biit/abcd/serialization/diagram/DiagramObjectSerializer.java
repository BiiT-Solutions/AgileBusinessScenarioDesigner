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

import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramObjectSerializer<T extends DiagramObject> extends CustomSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().getJsonType());
        }
        if (src.getId() != null) {
            jgen.writeNumberField("databaseId", src.getId());
            //Remove id as JointJs has already the jointjsid on this field.
            src.setId(null);
        }
        if (src.getJointjsId() != null) {
            jgen.writeStringField("id", src.getJointjsId());
        } else {
            jgen.writeStringField("id", src.getComparationId());
        }
        if (src.getEmbeds() != null) {
            jgen.writeStringField("embeds", src.getEmbeds());
        }
        jgen.writeNumberField("z", src.getZ());
        super.serialize(src, jgen);
    }
}
