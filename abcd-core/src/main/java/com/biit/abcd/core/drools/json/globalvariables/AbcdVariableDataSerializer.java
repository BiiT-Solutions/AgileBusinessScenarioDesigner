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

import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalcode;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.biit.drools.global.variables.DroolsVariableDataDate;
import com.biit.drools.global.variables.DroolsVariableDataNumber;
import com.biit.drools.global.variables.DroolsVariableDataPostalCode;
import com.biit.drools.global.variables.DroolsVariableDataText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AbcdVariableDataSerializer<T extends VariableData> implements JsonSerializer<T> {

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.add("type", context.serialize(equivalentDroolsClass(src)));
		jsonObject.add("value", context.serialize(src.getValue()));
		jsonObject.add("validFrom", context.serialize(src.getValidFrom()));
		jsonObject.add("validTo", context.serialize(src.getValidTo()));

		return jsonObject;
	}
	
	private String equivalentDroolsClass(T classType){
		if(classType instanceof VariableDataNumber){
			return DroolsVariableDataNumber.class.getName();
		} else if(classType instanceof VariableDataText){
			return DroolsVariableDataText.class.getName();
		} else if(classType instanceof VariableDataDate){
			return DroolsVariableDataDate.class.getName();
		} else if(classType instanceof VariableDataPostalcode){
			return DroolsVariableDataPostalCode.class.getName();
		} 
		return "";
	}

}
