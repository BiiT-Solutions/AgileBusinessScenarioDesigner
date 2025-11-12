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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionPluginMethodDeserializer extends ExpressionDeserializer<ExpressionPluginMethod> {

    @Override
    public void deserialize(ExpressionPluginMethod element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            if (jsonObject.get("pluginInterface") != null) {
                element.setPluginInterface(Class.forName(jsonObject.get("pluginInterface").textValue()));
            }
        } catch (ClassNotFoundException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
        element.setPluginName(parseString("pluginName", jsonObject));
        element.setPluginMethodName(parseString("pluginMethodName", jsonObject));
    }
}
