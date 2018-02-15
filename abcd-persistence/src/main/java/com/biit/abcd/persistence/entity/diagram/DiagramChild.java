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
	private static final long serialVersionUID = -5504613126614498746L;
	@OneToOne(fetch = FetchType.EAGER)
	private Diagram diagram;

	public DiagramChild() {
		super();
		DiagramText biitText = new DiagramText();
		biitText.setText("Diagram");
		setText(biitText);
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
			if (diagramChild.getDiagram() != null) {
				Diagram childDiagram = new Diagram();
				childDiagram.copyData(diagramChild.getDiagram());
				setDiagram(childDiagram);
			} else {
				setDiagram(null);
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramChild.");
		}
	}
}