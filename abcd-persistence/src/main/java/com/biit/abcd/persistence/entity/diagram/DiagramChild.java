package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_child")
public class DiagramChild extends DiagramElement {

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
		if (childDiagram != null) {
			innerStorableObjects.add(childDiagram);
			innerStorableObjects.addAll(childDiagram.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramChild) {
			super.copyData(object);
			DiagramChild diagramChild = (DiagramChild) object;

			Diagram childDiagram = new Diagram();
			childDiagram.copyData(diagramChild.getChildDiagram());
			setChildDiagram(childDiagram);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramChild.");
		}
	}
}