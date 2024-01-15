package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueNumberDeserializer extends ExpressionValueDeserializer<ExpressionValueNumber> {

    @Override
    public void deserialize(ExpressionValueNumber element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(parseDouble("value", jsonObject));
    }
}