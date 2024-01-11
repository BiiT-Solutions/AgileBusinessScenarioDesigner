package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class SizeSerializer extends CustomSerializer<Size> {

    @Override
    public void serialize(Size src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("height", String.valueOf(src.getHeight()));
        jgen.writeStringField("width", String.valueOf(src.getWidth()));
    }
}
