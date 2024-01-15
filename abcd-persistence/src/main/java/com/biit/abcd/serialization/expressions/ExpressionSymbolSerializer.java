package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionSymbolSerializer extends ExpressionSerializer<ExpressionSymbol> {

    @Override
    public void serialize(ExpressionSymbol src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue().name());
    }
}
