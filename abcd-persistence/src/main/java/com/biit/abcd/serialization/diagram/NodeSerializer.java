package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class NodeSerializer extends StorableObjectSerializer<Node> {

    @Override
    public void serialize(Node src, JsonGenerator jgen) throws IOException {
        jgen.writeStringField("id", src.getJointjsId());
        if (src.getId() != null) {
            jgen.writeNumberField("databaseId", src.getId());
            src.setId(null);
        }
        jgen.writeStringField("selector", src.getSelector());
        jgen.writeStringField("port", src.getPort());
        super.serialize(src, jgen);
    }
}
