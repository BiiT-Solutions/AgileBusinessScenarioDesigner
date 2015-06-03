package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.Form;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class FormSerializer extends BaseFormSerializer<Form> {

	@Override
	public JsonElement serialize(Form src, Type typeOfSrc,
			JsonSerializationContext context) {
		return (JsonObject) super.serialize(src,	typeOfSrc, context);
	}

}
