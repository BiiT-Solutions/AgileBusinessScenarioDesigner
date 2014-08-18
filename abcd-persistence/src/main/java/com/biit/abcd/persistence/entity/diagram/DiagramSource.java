package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "diagram_source")
public class DiagramSource extends DiagramElement {

	public DiagramSource() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Start");
		setBiitText(biitText);
	}
}
