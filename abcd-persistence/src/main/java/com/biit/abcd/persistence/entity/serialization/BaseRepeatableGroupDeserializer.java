package com.biit.abcd.persistence.entity.serialization;

import com.biit.form.entity.BaseRepeatableGroup;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BaseRepeatableGroupDeserializer<T extends BaseRepeatableGroup>
		extends TreeObjectDeserializer<T> {

	public void deserialize(JsonElement json,
			JsonDeserializationContext context, T element) {
		JsonObject jobject = (JsonObject) json;
		
		element.setRepeatable(parseBoolean("repeatable",jobject, context));

		super.deserialize(json, context, element);
	}

	public BaseRepeatableGroupDeserializer(Class<T> specificClass) {
		super(specificClass);
	}

}
