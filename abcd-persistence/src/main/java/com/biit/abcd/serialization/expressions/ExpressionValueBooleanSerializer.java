package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueBooleanSerializer extends ExpressionValueSerializer<ExpressionValueBoolean> {

    @Override
    public void serialize(ExpressionValueBoolean src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", String.valueOf(src.getValue()));
    }
}
