package com.biit.abcd.serialization.globalvariables;

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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalVariableDeserializer extends StorableObjectDeserializer<GlobalVariable> {

    @Override
    public void deserialize(GlobalVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            element.setName(parseString("name", jsonObject));
        } catch (FieldTooLongException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        if (jsonObject.get("answerFormat") != null) {
            element.setFormat(AnswerFormat.get(jsonObject.get("answerFormat").textValue()));
        }

        // Diagram objects deserialization
        final JsonNode variableData = jsonObject.get("variableData");
        if (variableData != null) {
            //Handle children one by one.
            if (variableData.isArray()) {
                final List<VariableData> data = new ArrayList<>();
                for (JsonNode childNode : variableData) {
                    try {
                        final Class<? extends VariableData> classType = (Class<? extends VariableData>) Class.forName(childNode.get("class").asText());
                        data.add(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid VariableData object:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
                element.setVariableData(data);
            }
        }
    }
}
