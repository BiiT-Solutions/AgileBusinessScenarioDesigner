package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_child")
public class DiagramChild extends DiagramElement {

	@OneToOne(fetch = FetchType.EAGER)
	private Diagram diagram;

	public DiagramChild() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Diagram");
		setBiitText(biitText);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (diagram != null) {
			diagram.resetIds();
		}
	}

	public Diagram getDiagram() {
		return diagram;
	}

	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (diagram != null) {
			innerStorableObjects.add(diagram);
			innerStorableObjects.addAll(diagram.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramChild) {
			super.copyData(object);
			DiagramChild diagramChild = (DiagramChild) object;

			Diagram childDiagram = new Diagram();
			childDiagram.copyData(diagramChild.getDiagram());
			setDiagram(childDiagram);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramChild.");
		}
	}
}