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
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramLinkDeserializer extends DiagramObjectDeserializer<DiagramLink> {

    @Override
    public void deserialize(DiagramLink element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("expressionChain") != null) {
            element.setExpressionChain(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("expressionChain").toString(), ExpressionChain.class));
        }
        if (jsonObject.get("source") != null) {
            element.setSource(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("source").toString(), Node.class));
        }
        if (jsonObject.get("target") != null) {
            element.setTarget(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("target").toString(), Node.class));
        }
        element.setText(parseString("text", jsonObject));
        element.setManhattan(parseBoolean("manhattan", jsonObject));
        if (jsonObject.get("attrs") != null) {
            element.setAttrs(jsonObject.get("attrs").toString());
        }
        if (jsonObject.get("vertices") != null) {
            element.setVertices(jsonObject.get("vertices").toString());
        }
        element.setSmooth(parseBoolean("smooth", jsonObject));
    }

    public DiagramLink getObject() {
        return new DiagramLink();
    }
}
