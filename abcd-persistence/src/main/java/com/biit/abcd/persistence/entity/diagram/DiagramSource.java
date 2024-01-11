package com.biit.abcd.persistence.entity.diagram;

import com.biit.abcd.serialization.diagram.DiagramSourceDeserializer;
import com.biit.abcd.serialization.diagram.DiagramSourceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = DiagramSourceDeserializer.class)
@JsonSerialize(using = DiagramSourceSerializer.class)
@Table(name = "diagram_source")
public class DiagramSource extends DiagramElement {
    private static final long serialVersionUID = -3121585662155609273L;

    public DiagramSource() {
        super();
        DiagramText biitText = new DiagramText();
        biitText.setText("Start");
        setText(biitText);
    }
}
