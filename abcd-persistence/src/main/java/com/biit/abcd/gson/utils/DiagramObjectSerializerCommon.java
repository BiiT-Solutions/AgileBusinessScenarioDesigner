package com.biit.abcd.gson.utils;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class DiagramObjectSerializerCommon<T extends DiagramElement> {

	public JsonElement serialize(DiagramElement diagramElement, JsonObject jsonObject, JsonSerializationContext context) {
		try {
			jsonObject.addProperty("id", diagramElement.getJointjsId());
			jsonObject.addProperty("type", diagramElement.getType().getJsonType());
			jsonObject.addProperty("embeds", diagramElement.getEmbeds());
			jsonObject.addProperty("z", diagramElement.getZ());

			jsonObject.addProperty("tooltip", diagramElement.getTooltip());

			JsonElement jsonSize = context.serialize(diagramElement.getSize());
			jsonObject.add("size", jsonSize);
			JsonElement jsonPosition = context.serialize(diagramElement.getPosition());
			jsonObject.add("position", jsonPosition);
			jsonObject.addProperty("angle", diagramElement.getAngle());

			// Create biitText JsonNode
			JsonElement jsonBiitText = context.serialize(diagramElement.getText());
			// Create Attrs node and insert biitText
			JsonObject jsonAttrs = new JsonObject();
			jsonAttrs.add(".biitText", jsonBiitText);
			// Add Attrs to the jsonObject
			jsonObject.add("attrs", jsonAttrs);
		} catch (NullPointerException npe) {
			AbcdLogger.errorMessage(this.getClass().getName(), npe);
		}

		return jsonObject;
	}

}
