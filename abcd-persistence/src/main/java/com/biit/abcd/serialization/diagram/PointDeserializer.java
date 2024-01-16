package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class PointDeserializer extends CustomDeserializer<Point> {

    @Override
    public void deserialize(Point element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX(parseInteger("x", jsonObject));
        element.setY(parseInteger("y", jsonObject));
    }

    @Override
    public Point getObject() {
        return new Point();
    }
}