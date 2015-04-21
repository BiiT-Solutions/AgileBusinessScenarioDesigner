package com.biit.abcd.core.drools.json.globalvariables;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalCode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class AbcdVariableDataPostalCodeSerializer extends AbcdVariableDataSerializer<VariableDataPostalCode> {

	@Override
	public JsonElement serialize(VariableDataPostalCode src, Type typeOfSrc, JsonSerializationContext context) {
		return (JsonObject) super.serialize(src, typeOfSrc, context);
	}
}
