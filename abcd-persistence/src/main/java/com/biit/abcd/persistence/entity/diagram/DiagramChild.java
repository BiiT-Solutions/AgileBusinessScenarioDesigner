package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

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

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(childDiagram);
		innerStorableObjects.addAll(childDiagram.getAllInnerStorableObjects());
		return innerStorableObjects;
	}
}