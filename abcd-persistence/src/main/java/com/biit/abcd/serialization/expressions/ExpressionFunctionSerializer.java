package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionFunctionSerializer extends ExpressionSerializer<ExpressionFunction> {

    @Override
    public void serialize(ExpressionFunction src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue().name());
    }
}
