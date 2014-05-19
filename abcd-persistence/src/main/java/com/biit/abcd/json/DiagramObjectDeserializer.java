package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramObject;
import com.biit.abcd.persistence.entity.DiagramObject.Point;
import com.biit.abcd.persistence.entity.DiagramObject.Size;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramObjectDeserializer implements JsonDeserializer<DiagramObject> {

	@Override
	public DiagramObject deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final DiagramObject diagramObject = new DiagramObject();
		diagramObject.setType(jsonObject.get("type").getAsString());
		try {
			diagramObject.setTooltip(jsonObject.get("tooltip").getAsString());
		} catch (NullPointerException npe) {
			diagramObject.setTooltip("");
		}
		try {
			diagramObject.setZ(jsonObject.get("z").getAsInt());
		} catch (NullPointerException npe) {
			diagramObject.setZ(0);
		}
		try {
			diagramObject.setAngle(jsonObject.get("angle").getAsFloat());
		} catch (NullPointerException npe) {
			diagramObject.setAngle(0f);
		}
		diagramObject.setId(jsonObject.get("id").getAsString());

		Gson g = new Gson();
		Point position = g.fromJson(jsonObject.get("position"), Point.class);
		diagramObject.setPosition(position);
		Size size = g.fromJson(jsonObject.get("size"), Size.class);
		diagramObject.setSize(size);

		return diagramObject;
	}

}
