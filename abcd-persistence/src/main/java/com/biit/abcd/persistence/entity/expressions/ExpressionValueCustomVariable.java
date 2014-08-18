package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.form.TreeObject;

/**
 * Defines a value as a already defined form custom variable.
 *
 */
@Entity
@Table(name = "expression_value_custom_variable")
public class ExpressionValueCustomVariable extends ExpressionValueTreeObjectReference {

	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	public ExpressionValueCustomVariable() {
		super();
	}

	public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable) {
		super();
		setReference(reference);
		this.variable = variable;
	}

	public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable, QuestionUnit dateUnit) {
		super();
		setReference(reference);
		setUnit(dateUnit);
		this.variable = variable;
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((getReference() != null) && (variable != null)) {
			expressionString += getReference().getName();
			if (getUnit() != null) {
				expressionString += "." + variable.getName() + " (" + getUnit().getAbbreviature() + ")";
			} else {
				expressionString += "." + variable.getName();
			}
		}
		return expressionString;
	}

	public CustomVariable getVariable() {
		return variable;
	}

	public void setVariable(CustomVariable variable) {
		this.variable = variable;
	}

	@Override
	protected String getExpression() {
		String expressionString = new String();
		expressionString += getReference().getName();
		if (variable != null) {
			expressionString += "." + variable.getName();
		}
		return expressionString;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueCustomVariable copy = new ExpressionValueCustomVariable();
		copy.setReference(getReference());
		copy.variable = variable;
		return copy;
	}

}
