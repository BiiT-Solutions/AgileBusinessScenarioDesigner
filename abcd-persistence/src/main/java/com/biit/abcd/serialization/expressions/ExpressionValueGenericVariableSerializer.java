package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueGenericVariableSerializer extends ExpressionValueSerializer<ExpressionValueGenericCustomVariable> {

    @Override
    public void serialize(ExpressionValueGenericCustomVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("type", src.getType().name());

    }
}
