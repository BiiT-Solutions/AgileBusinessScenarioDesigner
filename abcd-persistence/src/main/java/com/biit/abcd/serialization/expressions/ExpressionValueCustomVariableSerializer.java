package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueCustomVariableSerializer extends ExpressionValueSerializer<ExpressionValueCustomVariable> {

    @Override
    public void serialize(ExpressionValueCustomVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getVariable() != null) {
            jgen.writeStringField("variableId", src.getVariable().getComparationId());
        }
        if (src.getReference() != null) {
            jgen.writeStringField("referenceId", src.getReference().getComparationId());
        }
    }
}
