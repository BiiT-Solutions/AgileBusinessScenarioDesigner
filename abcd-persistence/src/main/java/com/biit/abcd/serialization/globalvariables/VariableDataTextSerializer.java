package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class VariableDataTextSerializer extends VariableDataSerializer<VariableDataText> {

    @Override
    public void serialize(VariableDataText src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue());
    }
}
