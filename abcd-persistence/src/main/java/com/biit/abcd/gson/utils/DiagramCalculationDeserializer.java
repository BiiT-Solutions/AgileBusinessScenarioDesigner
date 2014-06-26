package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramCalculationDeserializer extends DiagramObjectDeserializerCommon<DiagramCalculation> implements
		JsonDeserializer<DiagramCalculation> {

	public DiagramCalculationDeserializer() {
		super(DiagramCalculation.class);
	}

	@Override
	public DiagramCalculation deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		return deserialize(jsonObject, context);
	}

}
