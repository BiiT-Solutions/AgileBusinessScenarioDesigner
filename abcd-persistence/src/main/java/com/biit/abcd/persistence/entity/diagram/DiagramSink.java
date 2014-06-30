package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DIAGRAM_SINK")
public class DiagramSink  extends DiagramElement{
	
	public DiagramSink() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Sink");
		setBiitText(biitText);
	}
}