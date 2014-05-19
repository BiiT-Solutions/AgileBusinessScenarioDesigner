package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramSerializer implements JsonSerializer<Diagram> {

	@Override
	public JsonElement serialize(Diagram diagram, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		final JsonElement jsonDiagramObjects = context.serialize(diagram.getDiagramElements());
		jsonObject.add("cells", jsonDiagramObjects);

		return jsonObject;
	}
}