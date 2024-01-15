package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionFunctionDeserializer extends ExpressionDeserializer<ExpressionFunction> {

    @Override
    public void deserialize(ExpressionFunction element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("value") != null) {
            element.setValue(AvailableFunction.get(jsonObject.get("value").textValue()));
        }
    }
}