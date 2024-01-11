package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramSerializer extends StorableObjectSerializer<Diagram> {

    @Override
    public void serialize(Diagram src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("name", src.getName());

        jgen.writeFieldName("diagramObjects");
        jgen.writeStartArray("diagramObjects");
        for (DiagramObject child : src.getDiagramObjects()) {
            jgen.writeObject(child);
        }
        jgen.writeEndArray();
    }
}
