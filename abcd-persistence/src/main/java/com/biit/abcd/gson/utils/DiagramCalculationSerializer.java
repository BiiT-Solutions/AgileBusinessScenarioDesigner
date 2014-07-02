package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramCalculationSerializer extends DiagramObjectSerializerCommon<DiagramCalculation> implements
		JsonSerializer<DiagramCalculation> {

	@Override
	public JsonElement serialize(DiagramCalculation element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the question.
		if (element.getFormExpression() != null) {
			element.getBiitText().setText(element.getFormExpression().getName());
		}
		return serialize(element, jsonObject, context);
	}
}
