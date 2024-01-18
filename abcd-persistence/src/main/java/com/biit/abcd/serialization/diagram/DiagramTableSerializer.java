package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramTableSerializer extends DiagramElementSerializer<DiagramTable> {

    private static final String DEFAULT_NODE_NAME = "Table";

    @Override
    public void serialize(DiagramTable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        // Set the current text value.
        if (src.getTable() != null) {
            src.getText().setText(src.getTable().getName());
        } else {
            src.getText().setText(DEFAULT_NODE_NAME);
        }
        if (src.getTable() != null) {
            jgen.writeStringField("tableId", src.getTable().getComparationId());
        }
    }
}
