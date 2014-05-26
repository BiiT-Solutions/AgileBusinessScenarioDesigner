package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramBiitText;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramElementDeserializer implements JsonDeserializer<DiagramElement> {

	@Override
	public DiagramElement deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		DiagramElement diagramElement = new DiagramElement();

		diagramElement.setJointjsId(JsonUtils.getStringValue(jsonObject, "id"));
		diagramElement.setType(JsonUtils.getStringValue(jsonObject, "type"));
		diagramElement.setEmbeds(JsonUtils.getStringValue(jsonObject, "embeds"));
		diagramElement.setZ(JsonUtils.getIntValue(jsonObject, "z"));

		diagramElement.setTooltip(JsonUtils.getStringValue(jsonObject, "tooltip"));

		JsonElement sizeElement = jsonObject.get("size");
		if (sizeElement != null) {
			Size size = context.deserialize(sizeElement, Size.class);
			diagramElement.setSize(size);
		}
		JsonElement positionElement = jsonObject.get("position");
		if (positionElement != null) {
			Point point = context.deserialize(positionElement, Point.class);
			diagramElement.setPosition(point);
		}
		diagramElement.setAngle(JsonUtils.getFloatValue(jsonObject, "angle"));
		
		JsonElement attrs = jsonObject.get("attrs");
		if(attrs!=null){
			JsonObject attrsObject = attrs.getAsJsonObject();
			JsonElement biitTextElement = attrsObject.get(".biitText");
			DiagramBiitText biitText = context.deserialize(biitTextElement, DiagramBiitText.class);
			diagramElement.setBiitText(biitText);
		}		

		return diagramElement;
	}

}
