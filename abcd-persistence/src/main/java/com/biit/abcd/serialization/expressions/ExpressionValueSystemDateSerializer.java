package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.biit.form.jackson.serialization.CustomDeserializer.TIMESTAMP_FORMAT;

public class ExpressionValueSystemDateSerializer extends ExpressionValueSerializer<ExpressionValueSystemDate> {

    @Override
    public void serialize(ExpressionValueSystemDate src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", new SimpleDateFormat(TIMESTAMP_FORMAT).format(src.getValue()));
    }
}
