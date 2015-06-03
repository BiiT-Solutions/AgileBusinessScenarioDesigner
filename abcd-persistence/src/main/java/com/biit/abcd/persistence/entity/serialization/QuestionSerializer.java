package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;

import com.biit.abcd.persistence.entity.Question;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class QuestionSerializer extends TreeObjectSerializer<Question> {

	@Override
	public JsonElement serialize(Question src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("answerType", context.serialize(src.getAnswerType()));
		jsonObject.add("answerFormat", context.serialize(src.getAnswerFormat()));

		return jsonObject;
	}

}
