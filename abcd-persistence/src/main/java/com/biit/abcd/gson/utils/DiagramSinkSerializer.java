package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramSinkSerializer extends DiagramObjectSerializerCommon<DiagramSink> implements
JsonSerializer<DiagramSink> {

	@Override
	public JsonElement serialize(DiagramSink element, Type type, JsonSerializationContext context) {

		if(element.getExpression()!=null){
			element.getBiitText().setText(element.getExpression().getName());
		}else{
			element.getBiitText().setText("End");
		}

		final JsonObject jsonObject = new JsonObject();
		return serialize(element, jsonObject, context);
	}
}