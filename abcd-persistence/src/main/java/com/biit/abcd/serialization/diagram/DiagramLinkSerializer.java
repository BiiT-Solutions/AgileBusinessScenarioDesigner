package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramLinkSerializer extends DiagramObjectSerializer<DiagramLink> {

    @Override
    public void serialize(DiagramLink src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getExpressionChain().getExpressions() != null && !src.getExpressionChain().getExpressions().isEmpty()) {
            jgen.writeObjectField("expressionChain", src.getExpressionChain());
        }
        jgen.writeObjectField("source", src.getSource());
        jgen.writeObjectField("target", src.getTarget());
        jgen.writeStringField("text", src.getText());
        jgen.writeBooleanField("smooth", src.isSmooth());
        jgen.writeBooleanField("manhattan", src.isSmooth());
        if (src.getAttrs() != null && !src.getAttrs().isEmpty()) {
            //jgen.writeObjectField("attrs", src.getAttrs());
            jgen.writeFieldName("attrs");
            jgen.writeRawValue(src.getAttrs());
        }
        if (src.getVertices() != null && !src.getVertices().isEmpty()) {
            jgen.writeObjectField("vertices", ObjectMapperFactory.getObjectMapper().readValue(src.getVertices(), Point[].class));
        }
    }
}
