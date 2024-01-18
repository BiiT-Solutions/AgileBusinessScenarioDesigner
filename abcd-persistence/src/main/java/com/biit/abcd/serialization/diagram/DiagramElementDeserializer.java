package com.biit.abcd.serialization.diagram;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramElementDeserializer<T extends DiagramElement> extends DiagramObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("size") != null) {
            element.setSize(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("size").toString(), Size.class));
        }
        if (jsonObject.get("position") != null) {
            element.setPosition(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("position").toString(), Point.class));
        }
        element.setAngle(parseFloat("angle", jsonObject));

        //Attrs has a text inside.
//        JsonNode attrs = jsonObject.get("attrs");
//        if (attrs != null) {
//            try {
//                JsonNode biitTextElement = attrs.get(".biitText");
//                if (biitTextElement != null) {
//                    DiagramText biitText = ObjectMapperFactory.getObjectMapper().readValue(biitTextElement.toPrettyString(), DiagramText.class);
//                    element.setText(biitText);
//                }
//            } catch (NullPointerException e) {
//                AbcdLogger.severe(this.getClass().getName(), "Invalid diagram object:\n" + jsonObject.toPrettyString());
//                AbcdLogger.errorMessage(this.getClass().getName(), e);
//                throw new RuntimeException(e);
//            }
//        }
    }
}