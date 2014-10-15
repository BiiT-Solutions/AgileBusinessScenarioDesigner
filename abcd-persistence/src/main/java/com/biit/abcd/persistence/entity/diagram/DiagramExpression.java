package com.biit.abcd.persistence.entity.diagram;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_calculation")
public class DiagramExpression extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ExpressionChain formExpression;

	public DiagramExpression() {
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

	@Override
	public void resetIds() {
		super.resetIds();
		if (formExpression != null) {
			formExpression.resetIds();
		}
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (formExpression != null) {
			innerStorableObjects.add(formExpression);
			innerStorableObjects.addAll(formExpression.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramExpression) {
			super.copyData(object);
			DiagramExpression diagramCalculation = (DiagramExpression) object;

			if (diagramCalculation.getFormExpression() != null) {
				ExpressionChain formExpression = new ExpressionChain();
				formExpression.copyData(diagramCalculation.getFormExpression());
				setFormExpression(formExpression);
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of DiagramCalculation.");
		}
	}
}