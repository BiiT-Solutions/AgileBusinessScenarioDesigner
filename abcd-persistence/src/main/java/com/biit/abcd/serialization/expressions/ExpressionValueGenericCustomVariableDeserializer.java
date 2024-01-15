package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueGenericCustomVariableDeserializer extends ExpressionValueDeserializer<ExpressionValueGenericCustomVariable> {

    @Override
    public void deserialize(ExpressionValueGenericCustomVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setType(GenericTreeObjectType.get(jsonObject.get("type").textValue()));
        element.setVariable(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("variable").textValue(), CustomVariable.class));

    }
}