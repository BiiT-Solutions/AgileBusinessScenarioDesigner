package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramExpressionSerializer extends DiagramElementSerializer<DiagramExpression> {

    private static final String DEFAULT_NODE_NAME = "Expression";

    @Override
    public void serialize(DiagramExpression src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getExpression() != null) {
            src.getText().setText(src.getExpression().getName());
        } else {
            src.getText().setText(DEFAULT_NODE_NAME);
        }
        if (src.getExpression() != null) {
            jgen.writeObjectField("expressionId", src.getExpression().getComparationId());
        }
    }
}
