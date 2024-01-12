package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramExpressionSerializer extends DiagramElementSerializer<DiagramExpression> {

    @Override
    public void serialize(DiagramExpression src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("expression", src.getExpression());
    }
}
