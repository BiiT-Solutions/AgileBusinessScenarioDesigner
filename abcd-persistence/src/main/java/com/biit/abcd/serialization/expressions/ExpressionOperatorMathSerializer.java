package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionOperatorMathSerializer extends ExpressionOperatorSerializer<ExpressionOperatorMath> {

    @Override
    public void serialize(ExpressionOperatorMath src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }
}
