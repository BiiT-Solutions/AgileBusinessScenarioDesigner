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

        //'cells' as comes from jscript
        jgen.writeFieldName("cells");
        jgen.writeStartArray("cells");
        for (DiagramObject child : src.getDiagramObjects()) {
            child.setId(null);
            jgen.writeObject(child);
        }
        jgen.writeEndArray();
    }
}
