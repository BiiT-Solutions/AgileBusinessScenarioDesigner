package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramElement;
import com.biit.abcd.persistence.entity.DiagramLink;
import com.biit.abcd.persistence.entity.DiagramObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramElementSerializer implements JsonSerializer<DiagramElement> {

	@Override
	public JsonElement serialize(DiagramElement diagramElement, Type type, JsonSerializationContext context) {
		if (diagramElement.getType().equals("link")) {
			return context.serialize(diagramElement, DiagramLink.class);
		}

		return context.serialize(diagramElement, DiagramObject.class);
	}

}
