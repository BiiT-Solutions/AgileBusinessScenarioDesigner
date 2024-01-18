package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueGenericVariableSerializer extends ExpressionValueSerializer<ExpressionValueGenericVariable> {

    @Override
    public void serialize(ExpressionValueGenericVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("type", src.getType().name());

    }
}
