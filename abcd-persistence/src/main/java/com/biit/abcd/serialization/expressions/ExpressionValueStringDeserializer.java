package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueStringDeserializer extends ExpressionValueDeserializer<ExpressionValueString> {

    @Override
    public void deserialize(ExpressionValueString element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(parseString("value", jsonObject));
    }
}