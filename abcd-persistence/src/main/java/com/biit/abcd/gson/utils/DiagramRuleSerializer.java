package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramRuleSerializer extends DiagramObjectSerializerCommon<DiagramRule> implements
		JsonSerializer<DiagramRule> {

	@Override
	public JsonElement serialize(DiagramRule element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the rule.
		if (element.getRule() != null) {
			element.getBiitText().setText(element.getRule().getName());
		} else {
			element.getBiitText().setText("Rule");
		}
		return serialize(element, jsonObject, context);
	}
}