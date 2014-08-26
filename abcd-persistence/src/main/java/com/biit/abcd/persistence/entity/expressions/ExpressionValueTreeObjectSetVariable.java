package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.TreeObjectSetVariable;

@Entity
@Table(name = "expression_value_tree_object_set_variable")
public class ExpressionValueTreeObjectSetVariable extends ExpressionValueTreeObjectReference {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObjectSetVariable variable;

	public ExpressionValueTreeObjectSetVariable() {
		super();
	}

	public ExpressionValueTreeObjectSetVariable(TreeObjectSetVariable variable) {
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

	public TreeObjectSetVariable getVariable() {
		return variable;
	}

	public void setVariable(TreeObjectSetVariable variable) {
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
		ExpressionValueTreeObjectSetVariable copy = new ExpressionValueTreeObjectSetVariable();
		copy.variable = variable;
		copy.setEditable(isEditable());
		return copy;
	}

}
