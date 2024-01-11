package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.DiagramRepeatDeserializer;
import com.biit.abcd.serialization.diagram.DiagramRepeatSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = DiagramRepeatDeserializer.class)
@JsonSerialize(using = DiagramRepeatSerializer.class)
@Table(name = "diagram_repeat")
public class DiagramRepeat extends DiagramElement {
    private static final long serialVersionUID = -2219034744306658412L;

    public DiagramRepeat() {
        super();
        DiagramText biitText = new DiagramText();
        biitText.setText("Repeat");
        setText(biitText);
    }
}