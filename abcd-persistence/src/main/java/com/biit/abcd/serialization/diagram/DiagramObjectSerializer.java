package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramObjectSerializer<T extends DiagramObject> extends CustomSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().getJsonType());
        }
        if (src.getId() != null) {
            jgen.writeNumberField("databaseId", src.getId());
            //Remove id as JointJs has already the jointjsid on this field.
            src.setId(null);
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
        super.serialize(src, jgen);
    }
}
