package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
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