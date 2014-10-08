package com.biit.abcd.persistence.entity.diagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_fork")
public class DiagramFork extends DiagramElement {

	// Due to bug (https://hibernate.atlassian.net/browse/HHH-5559) orphanRemoval is not working correctly in @OneToOne.
	// We change a @OneToMany list with only one element.
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ExpressionValueTreeObjectReference> references;

	public DiagramFork() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Fork");
		setBiitText(biitText);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (references != null) {
			for (ExpressionValueTreeObjectReference expressionValueTreeObjectReference : references) {
				expressionValueTreeObjectReference.resetIds();
			}
		}
	}

	public ExpressionValueTreeObjectReference getReference() {
		if ((references == null) || references.isEmpty()) {
			return null;
		}
		return references.get(0);
	}

	public void setReference(ExpressionValueTreeObjectReference reference) {
		if (this.references == null) {
			this.references = new ArrayList<>();
		}
		if (!this.references.isEmpty()) {
			this.references.clear();
		}
		this.references.add(reference);
	}

	/**
	 * When the Diagram Fork changes, all outgoing links must be updated. I.e. links first expression element must be
	 * updated.
	 */
	public void resetOutgoingLinks() {
		for (DiagramLink outLink : getOutgoingLinks()) {
			if (getReference() != null) {
				Expression expression = getReference().generateCopy();
				expression.setEditable(false);
				outLink.resetExpressions(expression);
			}
		}
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		for (ExpressionValueTreeObjectReference child : references) {
			innerStorableObjects.add(child);
			innerStorableObjects.addAll(child.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramFork) {
			super.copyBasicInfo(object);
			DiagramFork diagramFork = (DiagramFork) object;

			references.clear();
			for (ExpressionValueTreeObjectReference child : diagramFork.references) {
				ExpressionValueTreeObjectReference newReference = new ExpressionValueTreeObjectReference();
				newReference.copyData(child);
				references.add(newReference);
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramFork.");
		}
	}
}