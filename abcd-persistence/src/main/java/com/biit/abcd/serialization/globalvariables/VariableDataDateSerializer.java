package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.biit.form.jackson.serialization.CustomDeserializer.TIMESTAMP_FORMAT;

public class VariableDataDateSerializer extends VariableDataSerializer<VariableDataDate> {

    @Override
    public void serialize(VariableDataDate src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", new SimpleDateFormat(TIMESTAMP_FORMAT).format(src.getValue()));
    }
}
