package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramExpressionDeserializer extends DiagramElementDeserializer<DiagramExpression> {

    @Override
    public void deserialize(DiagramExpression element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("expression") != null) {
            element.setExpression(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("expression").textValue(), ExpressionChain.class));
        }
    }
}