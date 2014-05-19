package com.biit.abcd.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.Diagram;
import com.biit.abcd.persistence.entity.DiagramObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class DiagramDeserializer implements JsonDeserializer<Diagram> {

	@Override
	public Diagram deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final Diagram diagram = new Diagram();
		
		Gson g = new Gson();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DiagramObject.class, new DiagramObjectDeserializer());
		Gson gson = gsonBuilder.create();

		Type listType = new TypeToken<ArrayList<DiagramObject>>() {
		}.getType();
		List<DiagramObject> objects = gson.fromJson(jsonObject.get("cells"), listType);
		diagram.setDiagramObjects(objects);

		return diagram;
	}

}
