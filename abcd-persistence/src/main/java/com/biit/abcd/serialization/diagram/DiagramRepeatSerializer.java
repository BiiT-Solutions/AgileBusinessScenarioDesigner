package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramRepeatSerializer extends StorableObjectSerializer<DiagramRepeat> {

    @Override
    public void serialize(DiagramRepeat src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }
}
