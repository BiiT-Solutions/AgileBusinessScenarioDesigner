package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionValueGlobalVariableDeserializer extends ExpressionValueDeserializer<ExpressionValueGlobalVariable> {

    @Override
    public void deserialize(ExpressionValueGlobalVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValue(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("globalVariable").textValue(), GlobalVariable.class));

    }
}