package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.biit.form.jackson.serialization.CustomDeserializer.TIMESTAMP_FORMAT;

public class VariableDataSerializer<T extends VariableData> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("validTo", new SimpleDateFormat(TIMESTAMP_FORMAT).format(src.getValidTo()));
    }
}
