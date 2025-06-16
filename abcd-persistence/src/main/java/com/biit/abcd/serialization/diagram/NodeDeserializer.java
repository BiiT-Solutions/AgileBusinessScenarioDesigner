package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NodeDeserializer extends StorableObjectDeserializer<Node> {

    @Override
    public void deserialize(Node element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setJointjsId(parseString("id", jsonObject));
        element.setId(parseLong("databaseId", jsonObject));
        element.setSelector(parseString("selector", jsonObject));
        element.setPort(parseString("port", jsonObject));
    }

    public Node getObject() {
        return new Node();
    }
}