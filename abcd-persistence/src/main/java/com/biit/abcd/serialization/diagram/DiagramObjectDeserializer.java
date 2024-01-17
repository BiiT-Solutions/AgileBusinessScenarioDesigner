package com.biit.abcd.serialization.diagram;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRepeat;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.form.log.FormStructureLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DiagramObjectDeserializer<T extends DiagramObject> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("type") != null) {
            element.setType(DiagramObjectType.getByJsonType(jsonObject.get("type").textValue()));
        }
        element.setJointjsId(parseString("id", jsonObject));
        element.setId(parseLong("databaseId", jsonObject));
        element.setEmbeds(parseString("embeds", jsonObject));
        if (parseInteger("z", jsonObject) != null) {
            element.setZ(parseInteger("z", jsonObject));
        }
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final DiagramObjectType diagramObjectType = DiagramObjectType.get(jsonObject.get("type").textValue());
        final Class<?> classType;
        switch (diagramObjectType) {
            case LINK:
                classType = DiagramLink.class;
                break;
            case CALCULATION:
                classType = DiagramExpression.class;
                break;
            case FORK:
                classType = DiagramFork.class;
                break;
            case DIAGRAM_CHILD:
                classType = DiagramChild.class;
                break;
            case RULE:
                classType = DiagramRule.class;
                break;
            case SINK:
                classType = DiagramSink.class;
                break;
            case SOURCE:
                classType = DiagramSource.class;
                break;
            case TABLE:
                classType = DiagramTable.class;
                break;
            case REPEAT:
                classType = DiagramRepeat.class;
                break;
            default:
                AbcdLogger.severe(this.getClass().getName(), "Invalid type found '" + diagramObjectType + "'.");
                return super.deserialize(jsonParser, deserializationContext);
        }
        try {
            final T element = (T) classType.getDeclaredConstructor().newInstance();
            deserialize(element, jsonObject, deserializationContext);
            return element;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException | NullPointerException e) {
            FormStructureLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
            FormStructureLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
    }
}