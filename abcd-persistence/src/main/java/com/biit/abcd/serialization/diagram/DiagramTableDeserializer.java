package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTableDeserializer extends DiagramElementDeserializer<DiagramTable> {

    @Override
    public void deserialize(DiagramTable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("table") != null) {
            element.setTable(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("table").toString(), TableRule.class));
        }
    }

    @Override
    public DiagramTable deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final DiagramTable element = new DiagramTable();
        deserialize(element, jsonObject, deserializationContext);
        return element;
    }
}
