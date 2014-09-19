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

@Entity
@Table(name = "diagram_fork")
public class DiagramFork extends DiagramElement {

	// Due to bug (https://hibernate.atlassian.net/browse/HHH-5559) orphanRemoval is not working correctly in @OneToOne.
	// We change a @OneToMany list with only one element.
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ExpressionValueTreeObjectReference> reference;

	public DiagramFork() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Fork");
		setBiitText(biitText);
	}

	public ExpressionValueTreeObjectReference getReference() {
		if ((reference == null) || reference.isEmpty()) {
			return null;
		}
		return reference.get(0);
	}

	public void setReference(ExpressionValueTreeObjectReference reference) {
		if (this.reference == null) {
			this.reference = new ArrayList<>();
		}
		if (!this.reference.isEmpty()) {
			this.reference.clear();
		}
		this.reference.add(reference);
	}

	/**
	 * When the Diagram Fork changes, all outgoing links must be updated. I.e. links first expression element must be
	 * updated.
	 */
	public void updateOutgoingLinks() {
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
		for (ExpressionValueTreeObjectReference child : reference) {
			innerStorableObjects.add(child);
			innerStorableObjects.addAll(child.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}
}