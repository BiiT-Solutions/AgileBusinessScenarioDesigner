package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValuePostalCodeSerializer extends ExpressionValueSerializer<ExpressionValuePostalCode> {

    @Override
    public void serialize(ExpressionValuePostalCode src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue());
    }
}
