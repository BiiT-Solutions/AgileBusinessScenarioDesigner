package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonParser;
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
        element.setAttrs(parseString("attrs", jsonObject));
        element.setVertices(parseString("vertices", jsonObject));
        element.setSmooth(parseBoolean("smooth", jsonObject));
    }

    @Override
    public DiagramLink getObject() {
        return new DiagramLink();
    }
}