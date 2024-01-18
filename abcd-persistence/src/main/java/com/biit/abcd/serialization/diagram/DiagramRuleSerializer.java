package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramRuleSerializer extends DiagramElementSerializer<DiagramRule> {

    private static final String DEFAULT_NODE_NAME = "Rule";

    @Override
    public void serialize(DiagramRule src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        // Set the current text value.
        if (src.getRule() != null) {
            src.getText().setText(src.getRule().getName());
        } else {
            src.getText().setText(DEFAULT_NODE_NAME);
        }
        if (src.getRule() != null) {
            jgen.writeObjectField("ruleId", src.getRule().getComparationId());
        }
    }
}
