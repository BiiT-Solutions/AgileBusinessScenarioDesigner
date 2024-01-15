package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionOperatorSerializer<T extends ExpressionOperator> extends ExpressionSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue().name());
    }
}
