package com.biit.abcd.json;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.DiagramLink;
import com.biit.abcd.persistence.entity.DiagramLink.Node;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DiagramLinkDeserializer implements JsonDeserializer<DiagramLink> {

	@Override
	public DiagramLink deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final DiagramLink diagramLink = new DiagramLink();
		diagramLink.setType(jsonObject.get("type").getAsString());

		try {
			diagramLink.setZ(jsonObject.get("z").getAsInt());
		} catch (NullPointerException npe) {
			diagramLink.setZ(0);
		}
		diagramLink.setId(jsonObject.get("id").getAsString());

		Gson g = new Gson();
		Node source = g.fromJson(jsonObject.get("source"), Node.class);
		diagramLink.setSource(source);
		Node target = g.fromJson(jsonObject.get("target"), Node.class);
		diagramLink.setTarget(target);

		return diagramLink;
	}

}
