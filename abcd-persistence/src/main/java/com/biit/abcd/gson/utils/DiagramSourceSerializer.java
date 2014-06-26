package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramSourceSerializer extends DiagramObjectSerializerCommon<DiagramSource> implements
		JsonSerializer<DiagramSource> {

	@Override
	public JsonElement serialize(DiagramSource element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		return serialize(element, jsonObject, context);
	}
}