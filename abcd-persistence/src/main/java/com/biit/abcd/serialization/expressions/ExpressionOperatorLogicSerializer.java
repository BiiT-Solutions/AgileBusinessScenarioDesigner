package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionOperatorLogicSerializer extends ExpressionOperatorSerializer<ExpressionOperatorLogic> {

    @Override
    public void serialize(ExpressionOperatorLogic src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }
}
