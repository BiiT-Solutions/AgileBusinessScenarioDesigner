package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class AnswerDeserializer extends TreeObjectDeserializer<Answer> {

    @Override
    public void deserialize(Answer element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }

}
