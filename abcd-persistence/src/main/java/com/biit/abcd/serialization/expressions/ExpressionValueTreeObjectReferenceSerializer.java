package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueTreeObjectReferenceSerializer extends ExpressionValueSerializer<ExpressionValueTreeObjectReference> {

    @Override
    public void serialize(ExpressionValueTreeObjectReference src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("referenceId", src.getReferenceId());
        jgen.writeStringField("unit", src.getUnit().name());
    }
}
