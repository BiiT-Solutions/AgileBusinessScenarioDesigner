package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramObjectSerializer implements JsonSerializer<DiagramObject> {

	@Override
	public JsonElement serialize(DiagramObject diagramElement, Type type, JsonSerializationContext context) {
		DiagramObjectType diagramObjectType = diagramElement.getType();

		switch (diagramObjectType) {
		case LINK:
			return context.serialize(diagramElement, DiagramLink.class);
		case CALCULATION:
			return context.serialize(diagramElement, DiagramCalculation.class);
		case FORK:
			return context.serialize(diagramElement, DiagramFork.class);
		case DIAGRAM_CHILD:
			return context.serialize(diagramElement, DiagramChild.class);
		case RULE:
			return context.serialize(diagramElement, DiagramRule.class);
		case SINK:
			return context.serialize(diagramElement, DiagramSink.class);
		case SOURCE:
			return context.serialize(diagramElement, DiagramSource.class);
		case TABLE:
			return context.serialize(diagramElement, DiagramTable.class);
		}

		return null;
	}
}
