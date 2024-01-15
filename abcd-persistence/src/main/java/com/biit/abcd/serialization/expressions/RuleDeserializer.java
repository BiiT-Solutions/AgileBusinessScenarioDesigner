package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class RuleDeserializer<T extends Rule> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setName(parseString("name", jsonObject));

        element.setConditions(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("conditions").asText(), ExpressionChain.class));
        element.setActions(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("actions").asText(), ExpressionChain.class));
    }
}