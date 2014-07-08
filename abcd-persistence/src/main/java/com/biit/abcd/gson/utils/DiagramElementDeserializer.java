package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramElementDeserializer implements JsonDeserializer<DiagramElement> {

	@SuppressWarnings("incomplete-switch")
	@Override
	public DiagramElement deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		final JsonObject jsonObject = json.getAsJsonObject();
		DiagramObjectType diagramObjectType = DiagramObjectType.getFromJsonType(jsonObject.get("type").getAsString());

		switch (diagramObjectType) {
		case CALCULATION:
			return context.deserialize(json, DiagramCalculation.class);
		case FORK:
			return context.deserialize(json, DiagramFork.class);
		case DIAGRAM_CHILD:
			return context.deserialize(json, DiagramChild.class);
		case RULE:
			return context.deserialize(json, DiagramRule.class);
		case SINK:
			return context.deserialize(json, DiagramSink.class);
		case SOURCE:
			return context.deserialize(json, DiagramSource.class);
		case TABLE:
			return context.deserialize(json, DiagramTable.class);
		}

		// If reaches this point then the type is unknown.
		throw new JsonParseException("Invalid type of diagram element");
	}
}