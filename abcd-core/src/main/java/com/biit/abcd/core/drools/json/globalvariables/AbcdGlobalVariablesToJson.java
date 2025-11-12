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

import java.util.List;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalcode;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class takes a java class and transforms it to a json string<br>
 * Used to convert the global variables array to a json string and store it
 */
public class AbcdGlobalVariablesToJson {

	public static String toJson(List<GlobalVariable> globalVariables) {
		if (globalVariables == null) {
			return "[]";
		} else {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(GlobalVariable.class, new AbcdGlobalVariableSerializer());
			gsonBuilder.registerTypeAdapter(VariableDataText.class, new AbcdVariableDataTextSerializer());
			gsonBuilder.registerTypeAdapter(VariableDataPostalcode.class, new AbcdVariableDataPostalCodeSerializer());
			gsonBuilder.registerTypeAdapter(VariableDataNumber.class, new AbcdVariableDataNumberSerializer());
			gsonBuilder.registerTypeAdapter(VariableDataDate.class, new AbcdVariableDataDateSerializer());
			Gson gson = gsonBuilder.create();
			return gson.toJson(globalVariables);
		}
	}
}
