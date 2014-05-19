package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramObjectSerializer implements JsonSerializer<DiagramObject> {

	@Override
	public JsonElement serialize(DiagramObject diagramElement, Type type, JsonSerializationContext context) {
		if (diagramElement.getType().equals("link")) {
			return context.serialize(diagramElement, DiagramLink.class);
		}

		return context.serialize(diagramElement, DiagramElement.class);
	}

}
