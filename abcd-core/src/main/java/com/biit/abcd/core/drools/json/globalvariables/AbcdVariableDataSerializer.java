package com.biit.abcd.core.drools.json.globalvariables;

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