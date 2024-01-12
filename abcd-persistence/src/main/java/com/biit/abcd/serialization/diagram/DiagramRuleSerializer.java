package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramRuleSerializer extends DiagramElementSerializer<DiagramRule> {

    @Override
    public void serialize(DiagramRule src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("rule", src.getRule());
    }
}
