package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueSystemDateDeserializer extends ExpressionValueDeserializer<ExpressionValueSystemDate> {

    @Override
    public void deserialize(ExpressionValueSystemDate element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(parseTimestamp("value", jsonObject));
    }
}