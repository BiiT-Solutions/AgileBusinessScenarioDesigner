package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueTreeObjectReferenceDeserializer extends ExpressionValueDeserializer<ExpressionValueTreeObjectReference> {

    @Override
    public void deserialize(ExpressionValueTreeObjectReference element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setReferenceId(parseString("referenceId", jsonObject));
        if (jsonObject.get("unit") != null) {
            element.setUnit(QuestionDateUnit.get(jsonObject.get("unit").textValue()));
        }
    }
}