package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramElementSerializer implements JsonSerializer<DiagramElement> {

	@Override
	public JsonElement serialize(DiagramElement diagramElement, Type type, JsonSerializationContext context) {

		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("id", diagramElement.getJointjsId());
		jsonObject.addProperty("type", diagramElement.getType());
		jsonObject.addProperty("embeds", diagramElement.getEmbeds());
		jsonObject.addProperty("z", diagramElement.getZ());

		jsonObject.addProperty("tooltip", diagramElement.getTooltip());

		JsonElement jsonSize = context.serialize(diagramElement.getSize());
		jsonObject.add("size", jsonSize);
		JsonElement jsonPosition = context.serialize(diagramElement.getPosition());
		jsonObject.add("position", jsonPosition);
		jsonObject.addProperty("angle", diagramElement.getAngle());

		// Create biitText JsonNode
		JsonElement jsonBiitText = context.serialize(diagramElement.getBiitText());
		// Create Attrs node and insert biitText
		JsonObject jsonAttrs = new JsonObject();
		jsonAttrs.add(".biitText", jsonBiitText);
		// Add Attrs to the jsonObject
		jsonObject.add("attrs", jsonAttrs);

		return jsonObject;
	}

}
