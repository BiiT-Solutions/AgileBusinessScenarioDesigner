package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramSourceSerializer extends CustomSerializer<DiagramSource> {

    @Override
    public void serialize(DiagramSource src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }
}
