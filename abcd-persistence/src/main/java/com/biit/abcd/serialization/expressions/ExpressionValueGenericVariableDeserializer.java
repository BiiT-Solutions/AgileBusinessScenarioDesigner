package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueGenericVariableDeserializer extends ExpressionValueDeserializer<ExpressionValueGenericVariable> {

    @Override
    public void deserialize(ExpressionValueGenericVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setType(GenericTreeObjectType.get(jsonObject.get("type").textValue()));
    }
}