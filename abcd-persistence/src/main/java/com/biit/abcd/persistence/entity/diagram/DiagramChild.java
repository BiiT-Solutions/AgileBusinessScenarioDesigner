package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagram_child")
public class DiagramChild extends DiagramElement {

	@OneToOne(fetch = FetchType.EAGER)
	private Diagram childDiagram;

	public DiagramChild() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Diagram");
		setBiitText(biitText);
	}

	public Diagram getChildDiagram() {
		return childDiagram;
	}

	public void setChildDiagram(Diagram childDiagram) {
		this.childDiagram = childDiagram;
	}
}