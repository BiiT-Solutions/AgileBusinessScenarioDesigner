package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueGenericCustomVariableSerializer extends ExpressionValueSerializer<ExpressionValueGenericCustomVariable> {

    @Override
    public void serialize(ExpressionValueGenericCustomVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("type", src.getType().name());
        if (src.getVariable() != null) {
            jgen.writeObjectField("variableId", src.getVariable().getComparationId());
        }
    }
}
