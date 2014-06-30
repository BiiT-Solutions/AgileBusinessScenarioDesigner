package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DIAGRAM_SOURCE")
public class DiagramSource extends DiagramElement {

	public DiagramSource() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Source");
		setBiitText(biitText);
	}
}
