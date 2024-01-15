package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionOperatorLogicDeserializer extends ExpressionOperatorDeserializer<ExpressionOperatorLogic> {

    @Override
    public void deserialize(ExpressionOperatorLogic element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}