package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueStringSerializer extends ExpressionValueSerializer<ExpressionValueString> {

    @Override
    public void serialize(ExpressionValueString src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue());
    }
}
