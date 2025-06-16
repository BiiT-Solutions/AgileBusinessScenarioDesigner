package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
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

    public DiagramExpression getObject() {
        return new DiagramExpression();
    }
}