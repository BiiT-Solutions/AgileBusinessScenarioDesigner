package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.persistence.entity.StorableObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class StorableObjectSerializer<T extends StorableObject> implements
		JsonSerializer<T> {

	@Override
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.add("class", context.serialize(src.getClass().getName()));
		jsonObject.add("comparationId", context.serialize(src.getComparationId()));
		jsonObject.add("creationTime", context.serialize(src.getCreationTime()));
		jsonObject.add("updateTime", context.serialize(src.getUpdateTime()));
		jsonObject.add("createdBy", context.serialize(src.getCreatedBy()));
		jsonObject.add("updatedBy", context.serialize(src.getUpdatedBy()));

		return jsonObject;
	}
}
