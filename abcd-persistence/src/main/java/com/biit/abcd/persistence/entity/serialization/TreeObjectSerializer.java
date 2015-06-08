package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.form.entity.TreeObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class TreeObjectSerializer<T extends TreeObject> extends StorableObjectSerializer<T> {

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("name", context.serialize(src.getName()));
		jsonObject.add("label", context.serialize(src.getLabel()));
		jsonObject.add("children", context.serialize(src.getAllNotHiddenChildren()));

		return jsonObject;
	}

}
