package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramSinkSerializer extends CustomSerializer<DiagramSink> {

    @Override
    public void serialize(DiagramSink src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }
}
