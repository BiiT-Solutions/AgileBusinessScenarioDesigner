package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramObjectDeserializer<T extends DiagramObject> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setType(DiagramObjectType.get(jsonObject.get("type").textValue()));
        element.setJointjsId(parseString("jointjsId", jsonObject));
        element.setEmbeds(parseString("embeds", jsonObject));
        if (parseInteger("z", jsonObject) != null) {
            element.setZ(parseInteger("z", jsonObject));
        }
    }
}