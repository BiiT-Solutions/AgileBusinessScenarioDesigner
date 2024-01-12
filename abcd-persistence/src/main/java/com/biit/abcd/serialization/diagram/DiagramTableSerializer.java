package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramTableSerializer extends DiagramElementSerializer<DiagramTable> {

    @Override
    public void serialize(DiagramTable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("table", src.getTable());
    }
}
