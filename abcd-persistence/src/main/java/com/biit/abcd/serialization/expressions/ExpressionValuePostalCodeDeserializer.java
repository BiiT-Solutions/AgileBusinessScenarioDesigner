package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValuePostalCodeDeserializer extends ExpressionValueDeserializer<ExpressionValuePostalCode> {

    @Override
    public void deserialize(ExpressionValuePostalCode element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(parseString("value", jsonObject));
    }
}