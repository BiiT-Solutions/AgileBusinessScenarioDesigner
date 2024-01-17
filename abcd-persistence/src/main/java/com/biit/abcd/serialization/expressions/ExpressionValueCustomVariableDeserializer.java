package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueCustomVariableDeserializer extends ExpressionValueDeserializer<ExpressionValueCustomVariable> {

    @Override
    public void deserialize(ExpressionValueCustomVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("variable") != null) {
            element.setVariableId(jsonObject.get("variable").toString());
        }

    }
}