package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramChildSerializer extends DiagramObjectSerializerCommon<DiagramChild> implements
		JsonSerializer<DiagramChild> {

	@Override
	public JsonElement serialize(DiagramChild element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the question.
		if (element.getChildDiagram() != null) {
			element.getBiitText().setText(element.getChildDiagram().getName());
		} else {
			element.getBiitText().setText("Diagram");
		}
		return serialize(element, jsonObject, context);
	}
}
