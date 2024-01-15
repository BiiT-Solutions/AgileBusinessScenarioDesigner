package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class QuestionDeserializer extends TreeObjectDeserializer<Question> {


    public void deserialize(Question element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("answerType") != null) {
            element.setAnswerType(AnswerType.get(jsonObject.get("answerType").textValue()));
        }
        try {
            if (jsonObject.get("answerFormat") != null) {
                final String answerFormat = jsonObject.get("answerFormat").textValue();
                if (answerFormat != null) {
                    element.setAnswerFormat(AnswerFormat.get(answerFormat));
                }
            }
        } catch (InvalidAnswerFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
