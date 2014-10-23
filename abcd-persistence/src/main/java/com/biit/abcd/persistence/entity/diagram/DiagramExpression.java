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

	@ManyToOne(fetch = FetchType.EAGER)
	private ExpressionChain expression;

	public DiagramExpression() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Calculation");
		setBiitText(biitText);
	}

	public ExpressionChain getExpression() {
		return expression;
	}

	public void setExpression(ExpressionChain expression) {
		this.expression = expression;
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (expression != null) {
			expression.resetIds();
		}
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (expression != null) {
			innerStorableObjects.add(expression);
			innerStorableObjects.addAll(expression.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramExpression) {
			super.copyData(object);
			DiagramExpression diagramCalculation = (DiagramExpression) object;

			if (diagramCalculation.getExpression() != null) {
				ExpressionChain formExpression = new ExpressionChain();
				formExpression.copyData(diagramCalculation.getExpression());
				setExpression(formExpression);
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of DiagramCalculation.");
		}
	}
}