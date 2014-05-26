package com.biit.abcd.gson.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {
	
	public static String getJsonStringArray(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsJsonArray().toString();
		}
		return null;
	}

	public static String getJsonString(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsJsonObject().toString();
		}
		return null;
	}
	
	public static String getStringValue(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsString();
		}
		return null;
	}
	
	public static int getIntValue(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsInt();
		}
		return 1;
	}
	
	public static float getFloatValue(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsFloat();
		}
		return 0.0f;
	}
	
	public static boolean getBooleanValue(JsonObject object, String memberName){
		JsonElement element = object.get(memberName);
		if(element!=null){
			return element.getAsBoolean();
		}
		return false;
	}
}
