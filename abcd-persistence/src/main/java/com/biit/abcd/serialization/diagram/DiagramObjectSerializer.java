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
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getJointjsId() != null) {
            jgen.writeStringField("jointjsId", src.getJointjsId());
        }
        if (src.getEmbeds() != null) {
            jgen.writeStringField("embeds", src.getEmbeds());
        }
        jgen.writeStringField("z", String.valueOf(src.getZ()));
    }
}
