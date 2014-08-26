package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.GenericTreeObjectVariable;

@Entity
@Table(name = "expression_value_generic_tree_object_variable")
public class ExpressionValueGenericTreeObjectVariable extends ExpressionValueTreeObjectReference {

	@ManyToOne(fetch = FetchType.EAGER)
	private GenericTreeObjectVariable variable;

	public ExpressionValueGenericTreeObjectVariable() {
		super();
	}

	public ExpressionValueGenericTreeObjectVariable(GenericTreeObjectVariable variable) {
		super();
		this.variable = variable;
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((variable != null)) {
			expressionString += variable.getScope().getName();
		}
		return expressionString;
	}

	public GenericTreeObjectVariable getVariable() {
		return variable;
	}

	public void setVariable(GenericTreeObjectVariable variable) {
		this.variable = variable;
	}

	@Override
	protected String getExpression() {
		String expressionString = new String();
		if (variable != null) {
			expressionString += variable.getScope().getName();
		}
		return expressionString;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueGenericTreeObjectVariable copy = new ExpressionValueGenericTreeObjectVariable();
		copy.variable = variable;
		copy.setEditable(isEditable());
		return copy;
	}

}
