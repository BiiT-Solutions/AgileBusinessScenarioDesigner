package com.biit.abcd.core.drools.json.globalvariables;

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
