package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramLinkSerializer implements JsonSerializer<DiagramLink> {

	@Override
	public JsonElement serialize(DiagramLink diagramElement, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("id", diagramElement.getJointjsId());		
		jsonObject.addProperty("type", diagramElement.getType().getJsonType());
		jsonObject.addProperty("embeds", diagramElement.getEmbeds());
		jsonObject.addProperty("z", diagramElement.getZ());

		jsonObject.add("source", context.serialize(diagramElement.getSource()));
		jsonObject.add("target", context.serialize(diagramElement.getTarget()));

		// Create Text node
		JsonObject textObject = new JsonObject();
		textObject.addProperty("text", diagramElement.getText());
		// Create Attrs
		JsonObject attrsObject = new JsonObject();
		attrsObject.add("text", textObject);
		// Create element of Label array
		JsonObject labelObject = new JsonObject();
		labelObject.add("attrs", attrsObject);
		labelObject.addProperty("position", "0.5");
		// Create array y add label object
		JsonArray labelsArray = new JsonArray();
		labelsArray.add(labelObject);
		jsonObject.add("labels", labelsArray);

		jsonObject.addProperty("smooth", diagramElement.isSmooth());
		jsonObject.addProperty("manhattan", diagramElement.isManhattan());

		// Parser to revive stored jsonStrings as jsonElements.
		JsonParser parser = new JsonParser();

		// Vertex array
		if (diagramElement.getVertices() != null) {
			jsonObject.add("vertices", parser.parse(diagramElement.getVertices()));
		}
		// Parse the attrs string to convert it to jsonObject.
		if (diagramElement.getAttrs() != null) {
			jsonObject.add("attrs", parser.parse(diagramElement.getAttrs()));
		}

		return jsonObject;
	}
}
