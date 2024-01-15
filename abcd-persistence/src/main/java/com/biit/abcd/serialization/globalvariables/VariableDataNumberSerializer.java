package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class VariableDataNumberSerializer extends VariableDataSerializer<VariableDataNumber> {

    @Override
    public void serialize(VariableDataNumber src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", String.valueOf(src.getValue()));
    }
}
