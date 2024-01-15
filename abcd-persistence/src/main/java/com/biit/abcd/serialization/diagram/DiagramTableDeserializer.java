package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTableDeserializer extends DiagramElementDeserializer<DiagramTable> {

    @Override
    public void deserialize(DiagramTable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("table") != null) {
            element.setTable(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("table").textValue(), TableRule.class));
        }
    }
}