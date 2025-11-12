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

import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTextDeserializer extends CustomDeserializer<DiagramText> {

    @Override
    public void deserialize(DiagramText element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setText(parseString("text", jsonObject));
        element.setFill(parseString("fill", jsonObject));
        //As comes from jscript
        element.setFontSize(parseString("font_size", jsonObject));
        element.setStroke(parseString("stroke", jsonObject));
        //As comes from jscript
        element.setStrokeWidth(parseString("stroke_width", jsonObject));
    }

    public DiagramText getObject() {
        return new DiagramText();
    }
}
