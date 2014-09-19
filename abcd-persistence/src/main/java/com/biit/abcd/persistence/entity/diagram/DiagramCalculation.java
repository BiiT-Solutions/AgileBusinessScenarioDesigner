package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "diagram_calculation")
public class DiagramCalculation extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private ExpressionChain formExpression;

	public DiagramCalculation() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}

	public ExpressionChain getFormExpression() {
		return formExpression;
	}

	public void setFormExpression(ExpressionChain formExpression) {
		this.formExpression = formExpression;
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(formExpression);
		innerStorableObjects.addAll(formExpression.getAllInnerStorableObjects());
		return innerStorableObjects;
	}
}