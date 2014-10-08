package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramElementSerializer implements JsonSerializer<DiagramElement> {

	@SuppressWarnings("incomplete-switch")
	@Override
	public JsonElement serialize(DiagramElement diagramElement, Type type, JsonSerializationContext context) {
		DiagramObjectType diagramObjectType = diagramElement.getType();

		switch (diagramObjectType) {
		case CALCULATION:
			return context.serialize(diagramElement, DiagramExpression.class);
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
		case REPEAT:
			return context.serialize(diagramElement, DiagramRepeat.class);
		}

		return null;
	}

}
