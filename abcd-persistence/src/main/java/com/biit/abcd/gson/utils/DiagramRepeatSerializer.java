package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramRepeatSerializer extends DiagramObjectSerializerCommon<DiagramRepeat> implements
		JsonSerializer<DiagramRepeat> {

	@Override
	public JsonElement serialize(DiagramRepeat element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the question.

		return serialize(element, jsonObject, context);
	}
}