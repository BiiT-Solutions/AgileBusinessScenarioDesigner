package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueBooleanDeserializer extends ExpressionValueDeserializer<ExpressionValueBoolean> {

    @Override
    public void deserialize(ExpressionValueBoolean element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(parseBoolean("value", jsonObject));
    }
}