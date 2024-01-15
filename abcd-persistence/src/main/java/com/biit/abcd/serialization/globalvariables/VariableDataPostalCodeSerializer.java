package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalcode;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class VariableDataPostalCodeSerializer extends VariableDataSerializer<VariableDataPostalcode> {

    @Override
    public void serialize(VariableDataPostalcode src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("value", src.getValue());
    }
}
