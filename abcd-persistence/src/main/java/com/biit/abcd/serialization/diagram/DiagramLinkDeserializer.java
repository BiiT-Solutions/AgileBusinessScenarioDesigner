package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramLinkDeserializer extends DiagramObjectDeserializer<DiagramLink> {

    @Override
    public void deserialize(DiagramLink element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("expressionChain") != null) {
            element.setExpressionChain(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("expressionChain").toString(), ExpressionChain.class));
        }
        if (jsonObject.get("source") != null) {
            element.setSource(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("source").toString(), Node.class));
        }
        if (jsonObject.get("target") != null) {
            element.setTarget(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("target").toString(), Node.class));
        }
        element.setText(parseString("text", jsonObject));
        element.setManhattan(parseBoolean("manhattan", jsonObject));
        if (jsonObject.get("attrs") != null) {
            element.setAttrs(jsonObject.get("attrs").toString());
        }
        if (jsonObject.get("vertices") != null) {
            element.setVertices(jsonObject.get("vertices").toString());
        }
        element.setSmooth(parseBoolean("smooth", jsonObject));
    }

    @Override
    public DiagramLink getObject() {
        return new DiagramLink();
    }
}