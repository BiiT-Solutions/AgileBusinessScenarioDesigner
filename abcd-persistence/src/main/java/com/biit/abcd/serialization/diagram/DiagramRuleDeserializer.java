package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramRuleDeserializer extends DiagramElementDeserializer<DiagramRule> {

    @Override
    public void deserialize(DiagramRule element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("rule") != null) {
            element.setRule(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("rule").textValue(), Rule.class));
        }
    }
}