package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramChildSerializer extends DiagramElementSerializer<DiagramChild> {

    private static final String DEFAULT_NODE_NAME = "Diagram";

    @Override
    public void serialize(DiagramChild src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getDiagram() != null) {
            src.getText().setText(src.getDiagram().getName());
        } else {
            src.getText().setText(DEFAULT_NODE_NAME);
        }
        jgen.writeObjectField("diagram", src.getDiagram());
    }
}
