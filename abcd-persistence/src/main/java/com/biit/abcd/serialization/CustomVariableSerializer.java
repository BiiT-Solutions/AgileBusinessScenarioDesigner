package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class CustomVariableSerializer extends StorableObjectSerializer<CustomVariable> {

    @Override
    public void serialize(CustomVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getName() != null) {
            jgen.writeStringField("name", src.getName());
        }
        if (src.getScope() != null) {
            jgen.writeStringField("scope", src.getScope().name());
        }
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getDefaultValue() != null) {
            jgen.writeStringField("defaultValue", src.getDefaultValue());
        }
    }
}
