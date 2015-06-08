package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.form.entity.BaseForm;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class BaseFormSerializer<T extends BaseForm> extends TreeObjectSerializer<T> {

	@Override
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("version", context.serialize(src.getVersion()));
		jsonObject.add("organizationId", context.serialize(src.getOrganizationId()));

		return jsonObject;
	}

	
}
