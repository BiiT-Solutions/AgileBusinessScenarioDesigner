package com.biit.abcd.gson.utils;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramObjectDeserializerCommon<T extends DiagramElement> {

	private Class<T> type;

	public DiagramObjectDeserializerCommon(Class<T> type) {
		this.type = type;
	}

	protected T deserialize(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
		T diagramElement = null;
		try {
			diagramElement = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		diagramElement.setJointjsId(JsonUtils.getStringValue(jsonObject, "id"));
		diagramElement.setType(DiagramObjectType.get(JsonUtils.getStringValue(jsonObject, "type")));
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
		if (attrs != null) {
			JsonObject attrsObject = attrs.getAsJsonObject();
			JsonElement biitTextElement = attrsObject.get(".biitText");
			if(biitTextElement!=null){
				DiagramText biitText = context.deserialize(biitTextElement, DiagramText.class);
				diagramElement.setText(biitText);
			}
		}

		return diagramElement;
	}
}
