package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.DiagramSinkDeserializer;
import com.biit.abcd.serialization.diagram.DiagramSinkSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = DiagramSinkDeserializer.class)
@JsonSerialize(using = DiagramSinkSerializer.class)
@Table(name = "diagram_sink")
public class DiagramSink extends DiagramExpression {
    private static final long serialVersionUID = 1993423029316963730L;

    public DiagramSink() {
        super();
    }

    @Override
    public DiagramText getText() {
        final DiagramText diagramText = new DiagramText();
        diagramText.setText("End");
        return diagramText;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DiagramSink) {
            super.copyData(object);
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramSink.");
        }
    }

}