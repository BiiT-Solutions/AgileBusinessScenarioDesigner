package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionSerializer<T extends Expression> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("sortSeq", String.valueOf(src.getSortSeq()));
    }
}
