package com.biit.abcd.serialization.diagram;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
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
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramDeserializer extends StorableObjectDeserializer<Diagram> {

    @Override
    public void deserialize(Diagram element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setName(parseString("name", jsonObject));

        // Diagram objects deserialization
        //'cells" as comes from jscript
        final JsonNode diagramObjects = jsonObject.get("cells");
        if (diagramObjects != null) {
            //Handle children one by one.
            if (diagramObjects.isArray()) {
                for (JsonNode childNode : diagramObjects) {
                    try {
                        final Class<? extends DiagramObject> classType;
                        if (childNode.get("class") != null) {
                            classType = (Class<? extends DiagramObject>) Class.forName(childNode.get("class").asText());
                        } else {
                            classType = (Class<? extends DiagramObject>) getNodeClass(childNode);
                        }
                        element.addDiagramObject(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid diagram object:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public Diagram deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final Diagram diagram = new Diagram();
        deserialize(diagram, jsonObject, deserializationContext);
        return diagram;
    }

    private Class<?> getNodeClass(JsonNode jsonObject) throws ClassNotFoundException {
        final DiagramObjectType diagramObjectType = DiagramObjectType.getByType(jsonObject.get("type").textValue());
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
                throw new ClassNotFoundException("");
        }
        return classType;
    }
}