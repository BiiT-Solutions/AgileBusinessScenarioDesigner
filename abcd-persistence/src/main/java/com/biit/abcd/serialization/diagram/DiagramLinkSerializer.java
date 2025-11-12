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

import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramLinkSerializer extends DiagramObjectSerializer<DiagramLink> {

    @Override
    public void serialize(DiagramLink src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getExpressionChain().getExpressions() != null && !src.getExpressionChain().getExpressions().isEmpty()) {
            jgen.writeObjectField("expressionChain", src.getExpressionChain());
        }
        jgen.writeObjectField("source", src.getSource());
        jgen.writeObjectField("target", src.getTarget());
        jgen.writeStringField("text", src.getText());
        jgen.writeBooleanField("smooth", src.isSmooth());
        jgen.writeBooleanField("manhattan", src.isSmooth());
        if (src.getAttrs() != null && !src.getAttrs().isEmpty()) {
            //jgen.writeObjectField("attrs", src.getAttrs());
            jgen.writeFieldName("attrs");
            jgen.writeRawValue(src.getAttrs());
        }
        if (src.getVertices() != null && !src.getVertices().isEmpty()) {
            jgen.writeObjectField("vertices", ObjectMapperFactory.getObjectMapper().readValue(src.getVertices(), Point[].class));
        }
    }
}
