package com.biit.abcd.persistence.entity.serialization;

import com.biit.abcd.persistence.entity.Answer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

public class AnswerDeserializer  extends TreeObjectDeserializer<Answer> {

	public AnswerDeserializer() {
		super(Answer.class);
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, Answer element) {
		super.deserialize(json, context, element);
	}
	
}
