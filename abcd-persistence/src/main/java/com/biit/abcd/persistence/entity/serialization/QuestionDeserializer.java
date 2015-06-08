package com.biit.abcd.persistence.entity.serialization;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class QuestionDeserializer extends TreeObjectDeserializer<Question> {

	public QuestionDeserializer() {
		super(Question.class);
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, Question element) {
		JsonObject jobject = (JsonObject) json;

		try {
			element.setAnswerType(parseAnswerType("answerType", jobject, context));
			element.setAnswerFormat(parseAnswerFormat("answerFormat", jobject, context));
		} catch (InvalidAnswerFormatException e) {
			throw new JsonParseException(e);
		}
		super.deserialize(json, context, element);
	}

	public static AnswerType parseAnswerType(String name, JsonObject jobject, JsonDeserializationContext context) {
		if (jobject.get(name) != null) {
			return (AnswerType) context.deserialize(jobject.get(name), AnswerType.class);
		}
		return null;
	}

	public static AnswerFormat parseAnswerFormat(String name, JsonObject jobject, JsonDeserializationContext context) {
		if (jobject.get(name) != null) {
			return (AnswerFormat) context.deserialize(jobject.get(name), AnswerFormat.class);
		}
		return null;
	}

}
