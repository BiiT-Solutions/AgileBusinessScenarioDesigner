package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramObjectSerializer<T extends DiagramObject> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().getJsonType());
        }
        if (src.getId() != null) {
            jgen.writeNumberField("databaseId", src.getId());
        }
        if (src.getJointjsId() != null) {
            jgen.writeStringField("id", src.getJointjsId());
        } else {
            jgen.writeStringField("id", src.getComparationId());
        }
        if (src.getEmbeds() != null) {
            jgen.writeStringField("embeds", src.getEmbeds());
        }
        jgen.writeNumberField("z", src.getZ());
    }
}
