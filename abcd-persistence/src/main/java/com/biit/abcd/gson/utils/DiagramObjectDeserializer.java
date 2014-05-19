package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramObjectDeserializer implements JsonDeserializer<DiagramObject> {

	@Override
	public DiagramObject deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		final JsonObject jsonObject = json.getAsJsonObject();
		if (jsonObject.get("type").getAsString().equals("link")) {
			return context.deserialize(json, DiagramLink.class);
		}

		return context.deserialize(json, DiagramElement.class);
	}
}
