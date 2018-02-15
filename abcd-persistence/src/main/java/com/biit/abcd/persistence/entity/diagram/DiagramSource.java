package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
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
