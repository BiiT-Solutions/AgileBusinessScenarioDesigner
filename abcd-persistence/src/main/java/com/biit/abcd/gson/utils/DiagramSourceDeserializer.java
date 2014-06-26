package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramSourceDeserializer extends DiagramObjectDeserializerCommon<DiagramSource> implements
		JsonDeserializer<DiagramSource> {

	public DiagramSourceDeserializer() {
		super(DiagramSource.class);
	}

	@Override
	public DiagramSource deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		return deserialize(jsonObject, context);
	}

}