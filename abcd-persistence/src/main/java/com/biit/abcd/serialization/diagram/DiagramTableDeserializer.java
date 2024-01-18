package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTableDeserializer extends DiagramElementDeserializer<DiagramTable> {

    @Override
    public void deserialize(DiagramTable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("tableId") != null) {
            element.setTableId(parseString("tableId", jsonObject));
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
