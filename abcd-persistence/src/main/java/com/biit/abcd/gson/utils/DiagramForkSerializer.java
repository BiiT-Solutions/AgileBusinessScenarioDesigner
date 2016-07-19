package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramForkSerializer extends DiagramObjectSerializerCommon<DiagramFork> implements
		JsonSerializer<DiagramFork> {

	@Override
	public JsonElement serialize(DiagramFork element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		//Set the current name of the question.
		if(element.getReference()!=null){
			element.getBiitText().setText(element.getReference().getRepresentation(true));
		}else{
			element.getBiitText().setText("Fork");
		}
		return serialize(element, jsonObject, context);
	}
}