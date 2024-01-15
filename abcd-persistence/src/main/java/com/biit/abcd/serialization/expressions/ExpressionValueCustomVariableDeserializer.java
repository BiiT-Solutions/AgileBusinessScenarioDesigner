package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueCustomVariableDeserializer extends ExpressionValueDeserializer<ExpressionValueCustomVariable> {

    @Override
    public void deserialize(ExpressionValueCustomVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("variable") != null) {
            element.setVariable(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("variable").textValue(), CustomVariable.class));
        }

    }
}