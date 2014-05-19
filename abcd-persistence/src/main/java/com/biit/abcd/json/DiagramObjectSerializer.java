package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramObjectSerializer implements JsonSerializer<DiagramObject> {

	@Override
	public JsonElement serialize(DiagramObject diagramObject, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", diagramObject.getType());
		jsonObject.addProperty("tooltip", diagramObject.getTooltip());
		jsonObject.addProperty("z", diagramObject.getZ());
		jsonObject.addProperty("angle", diagramObject.getAngle());
		jsonObject.addProperty("id", diagramObject.getId());
		jsonObject.add("position", context.serialize(diagramObject.getPosition()));
		jsonObject.add("size", context.serialize(diagramObject.getSize()));

		return jsonObject;
	}
}
