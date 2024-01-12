package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramForkSerializer extends DiagramElementSerializer<DiagramFork> {

    @Override
    public void serialize(DiagramFork src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeFieldName("references");
        jgen.writeStartArray("references");
        for (ExpressionValueTreeObjectReference reference : src.getReferences()) {
            jgen.writeObject(reference);
        }
        jgen.writeEndArray();
    }
}
