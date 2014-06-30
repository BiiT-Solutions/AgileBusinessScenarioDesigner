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
@Table(name = "EXPRESSION_VALUE_GLOBAL_VARIABLE")
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
	public String getExpressionTableString() {
		String expressionString = new String();
		if (constant != null) {
			expressionString += constant.getName();
		}
		return expressionString;
	}

	public GlobalVariable getVariable() {
		return constant;
	}

	public void setVariable(GlobalVariable variable) {
		this.constant = variable;
	}

	@Override
	protected String getExpression() {
		return getExpressionTableString();
	}

}
