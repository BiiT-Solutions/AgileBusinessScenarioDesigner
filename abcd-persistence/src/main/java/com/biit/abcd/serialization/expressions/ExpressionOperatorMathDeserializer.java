package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionOperatorMathDeserializer extends ExpressionOperatorDeserializer<ExpressionOperatorMath> {

    @Override
    public void deserialize(ExpressionOperatorMath element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}