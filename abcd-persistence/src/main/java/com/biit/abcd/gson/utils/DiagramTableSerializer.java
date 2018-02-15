package com.biit.abcd.gson.utils;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DiagramTableSerializer extends DiagramObjectSerializerCommon<DiagramTable> implements
		JsonSerializer<DiagramTable> {

	@Override
	public JsonElement serialize(DiagramTable element, Type type, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		// Set the current name of the question.
		if (element.getTable() != null) {
			element.getText().setText(element.getTable().getName());
		}else{
			element.getText().setText("Table");
		}
		return serialize(element, jsonObject, context);
	}
}