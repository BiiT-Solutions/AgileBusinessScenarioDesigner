package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramElementDeserializer<T extends DiagramElement> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setTooltip(parseString("tooltip", jsonObject));
        if (jsonObject.get("size") != null) {
            element.setSize(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("size").textValue(), Size.class));
        }
        if (jsonObject.get("position") != null) {
            element.setPosition(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("position").textValue(), Point.class));
        }
        element.setAngle(parseFloat("angle", jsonObject));
        if (jsonObject.get("text") != null) {
            element.setText(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("text").textValue(), DiagramText.class));
        }
    }
}