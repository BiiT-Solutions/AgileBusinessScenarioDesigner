package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramElement;
import com.biit.abcd.persistence.entity.DiagramLink;
import com.biit.abcd.persistence.entity.DiagramObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramElementDeserializer implements JsonDeserializer<DiagramElement> {

	@Override
	public DiagramElement deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		final JsonObject jsonObject = json.getAsJsonObject();
		if (jsonObject.get("type").getAsString().equals("link")) {
			return context.deserialize(json, DiagramLink.class);
		}

		return context.deserialize(json, DiagramObject.class);
	}
}
