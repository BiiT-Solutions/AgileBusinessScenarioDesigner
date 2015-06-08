package com.biit.abcd.persistence.entity.serialization;

import com.biit.form.entity.BaseForm;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BaseFormDeserializer<T extends BaseForm> extends TreeObjectDeserializer<T>{

	public BaseFormDeserializer(Class<T> specificClass) {
		super(specificClass);
	}

	@Override
	public void deserialize(JsonElement json,
			JsonDeserializationContext context, T element) {
		JsonObject jobject = (JsonObject) json;

		element.setVersion(parseInteger("version", jobject, context));
		element.setOrganizationId(parseLong("organizationId", jobject, context));

		super.deserialize(json, context, element);
	}
	
}
