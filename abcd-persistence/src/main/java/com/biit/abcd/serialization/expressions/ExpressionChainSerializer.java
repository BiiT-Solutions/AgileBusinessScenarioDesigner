package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionChainSerializer extends ExpressionSerializer<ExpressionChain> {

    @Override
    public void serialize(ExpressionChain src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());

        jgen.writeFieldName("expressions");
        jgen.writeStartArray("expressions");
        for (Expression child : src.getExpressions()) {
            jgen.writeObject(child);
        }
        jgen.writeEndArray();
    }
}
