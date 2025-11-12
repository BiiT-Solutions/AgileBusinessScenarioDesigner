package com.biit.abcd.core.drools.json.globalvariables;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class takes a java class and transforms it to a json string<br>
 * Used to convert the global variables array to a json string and store it
 */
public class AbcdGlobalVariableSerializer implements JsonSerializer<GlobalVariable> {

	@Override
	public JsonElement serialize(GlobalVariable src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.add("name", context.serialize(src.getName()));
		jsonObject.add("format", context.serialize(src.getFormat()));
		jsonObject.add("variableData", context.serialize(src.getVariableData()));

		return jsonObject;
	}
}
