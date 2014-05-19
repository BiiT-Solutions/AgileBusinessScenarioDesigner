package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramLink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramLinkSerializer implements JsonSerializer<DiagramLink> {

	@Override
	public JsonElement serialize(DiagramLink diagramLink, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", diagramLink.getType());
		jsonObject.addProperty("z", diagramLink.getZ());
		jsonObject.addProperty("id", diagramLink.getId());
		jsonObject.add("source", context.serialize(diagramLink.getSource()));
		jsonObject.add("target", context.serialize(diagramLink.getTarget()));

		return jsonObject;
	}
}
