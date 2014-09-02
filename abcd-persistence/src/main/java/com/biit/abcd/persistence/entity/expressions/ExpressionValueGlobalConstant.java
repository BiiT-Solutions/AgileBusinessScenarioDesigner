package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;

/**
 * Defines a value as a already defined Global Constant.
 *
 */
@Entity
@Table(name = "expression_value_global_variable")
public class ExpressionValueGlobalConstant extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private GlobalVariable constant;

	protected ExpressionValueGlobalConstant() {
		super();
	}

	public ExpressionValueGlobalConstant(GlobalVariable constant) {
		super();
		this.constant = constant;
	}

	@Override
	public String getRepresentation() {
		String expressionString = "";
		if (constant != null) {
			expressionString += constant.getName();
		}
		return expressionString;
	}

	public GlobalVariable getVariable() {
		return constant;
	}

	public void setVariable(GlobalVariable variable) {
		constant = variable;
	}

	@Override
	protected String getExpression() {
		return getRepresentation();
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueGlobalConstant copy = new ExpressionValueGlobalConstant();
		copy.constant = constant;
		return copy;
	}

	@Override
	public Object getValue() {
		return getVariable();
	}
}
