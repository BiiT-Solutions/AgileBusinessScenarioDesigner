package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramExpressionDeserializer extends DiagramElementDeserializer<DiagramExpression> {

    @Override
    public void deserialize(DiagramExpression element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("expressionId") != null) {
            element.setExpressionId(parseString("expressionId", jsonObject));
        }
    }

    @Override
    public DiagramExpression getObject() {
        return new DiagramExpression();
    }
}