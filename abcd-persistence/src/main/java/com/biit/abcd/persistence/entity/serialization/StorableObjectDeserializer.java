package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.persistence.entity.StorableObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class StorableObjectDeserializer<T extends StorableObject> implements JsonDeserializer<T>{
	
	public void deserialize(JsonElement json,JsonDeserializationContext context, T element){
		JsonObject jobject = (JsonObject) json;
		
		element.setComparationId(parseString("comparationId",jobject,context));
		element.setCreationTime(parseTimestamp("creationTime", jobject, context));
		element.setUpdateTime(parseTimestamp("updateTime", jobject, context));
		element.setCreatedBy(parseLong("createdBy", jobject, context));
		element.setUpdatedBy(parseLong("updatedBy", jobject, context));
	}
	
	public String parseString(String name,JsonObject jobject,JsonDeserializationContext context){
		if(jobject.get(name)!=null){
			return (String) context.deserialize(jobject.get(name), String.class);
		}
		return null;
	}
	public Timestamp parseTimestamp(String name,JsonObject jobject,JsonDeserializationContext context){
		if(jobject.get(name)!=null){
			return (Timestamp) context.deserialize(jobject.get(name), Timestamp.class);
		}
		return null;
	}
	
	public Long parseLong(String name,JsonObject jobject,JsonDeserializationContext context){
		if(jobject.get(name)!=null){
			return (Long) context.deserialize(jobject.get(name), Long.class);
		}
		return null;
	}
	
	public Integer parseInteger(String name, JsonObject jobject, JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (Integer) context.deserialize(jobject.get(name), Integer.class);
		}
		return null;
	}

	public boolean parseBoolean(String name, JsonObject jobject,
			JsonDeserializationContext context) {
		if(jobject.get(name)!=null){
			return (Boolean) context.deserialize(jobject.get(name), Boolean.class);
		}
		return false;
	}
	
	@Override
	public T deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		final JsonObject jsonObject = json.getAsJsonObject();
		Class<?> classType;
		try {
			classType = Class.forName(jsonObject.get("class").getAsString());
			return context.deserialize(json, classType);
		} catch (ClassNotFoundException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		
		return null;
	}

}
