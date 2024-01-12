package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramChildSerializer extends DiagramElementSerializer<DiagramChild> {

    @Override
    public void serialize(DiagramChild src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("diagram", src.getDiagram());
    }
}
