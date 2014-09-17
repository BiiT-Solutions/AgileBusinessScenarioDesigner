package com.biit.abcd.core.drools.json.globalvariables;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalCode;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@SuppressWarnings("rawtypes")
public class VariableDataDeserializer implements JsonDeserializer<VariableData> {

	private static Map<String, Class> map = new TreeMap<String, Class>();

	static {
		map.put("VariableDataNumber", VariableDataNumber.class);
		map.put("VariableDataDate", VariableDataDate.class);
		map.put("VariableDataText", VariableDataText.class);
		map.put("VariableDataPostalCode", VariableDataPostalCode.class);
	}

	@Override
	public VariableData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		String type = json.getAsJsonObject().get("isA").getAsString();
		Class c = map.get(type);
		if (c == null)
			throw new RuntimeException("Unknow class: " + type);
		return context.deserialize(json, c);
	}

}