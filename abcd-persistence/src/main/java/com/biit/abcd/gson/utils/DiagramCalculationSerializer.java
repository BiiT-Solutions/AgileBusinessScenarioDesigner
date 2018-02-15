package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramCalculationSerializer extends DiagramObjectSerializerCommon<DiagramExpression> implements
		JsonSerializer<DiagramExpression> {

	@Override
	public JsonElement serialize(DiagramExpression element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the question.
		if (element.getExpression() != null) {
			element.getText().setText(element.getExpression().getName());
		}else{
			element.getText().setText("Calculation");
		}
		return serialize(element, jsonObject, context);
	}
}
