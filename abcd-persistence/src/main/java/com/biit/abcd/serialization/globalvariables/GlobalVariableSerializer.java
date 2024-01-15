package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class GlobalVariableSerializer extends StorableObjectSerializer<GlobalVariable> {

    @Override
    public void serialize(GlobalVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("answerFormat", src.getFormat().name());

        jgen.writeFieldName("variableData");
        jgen.writeStartArray("variableData");
        for (VariableData variableData : src.getVariableData()) {
            jgen.writeObject(variableData);
        }
        jgen.writeEndArray();
    }
}
