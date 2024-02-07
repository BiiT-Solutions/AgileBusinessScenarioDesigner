package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueBoolean;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueNumberSerializer extends ExpressionValueSerializer<ExpressionValueNumber> {

    @Override
    public void serialize(ExpressionValueNumber src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeNumberField("value", src.getValue());
    }
}
