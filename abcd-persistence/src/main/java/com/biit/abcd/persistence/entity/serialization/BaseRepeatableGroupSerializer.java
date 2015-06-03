package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.form.entity.BaseRepeatableGroup;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class BaseRepeatableGroupSerializer<T extends BaseRepeatableGroup> extends TreeObjectSerializer<T> {

	@Override
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("repeatable", context.serialize(src.isRepeatable()));

		return jsonObject;
	}
}
