package com.biit.abcd.core.drools.json.globalvariables;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class AbcdVariableDataDateSerializer extends AbcdVariableDataSerializer<VariableDataDate> {

	@Override
	public JsonElement serialize(VariableDataDate src, Type typeOfSrc, JsonSerializationContext context) {
		return (JsonObject) super.serialize(src, typeOfSrc, context);
	}
}