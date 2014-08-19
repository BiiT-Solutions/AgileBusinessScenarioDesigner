package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "diagram_repeat")
public class DiagramRepeat extends DiagramElement {

	public DiagramRepeat() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Repeat");
		setBiitText(biitText);
	}
}