package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;

@Entity
@Table(name = "EXPRESSION_GLOBAL_VARIABLE")
public class ExprValueGlobalConstant extends ExprValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private GlobalVariable constant;

	protected ExprValueGlobalConstant() {
		super();
	}

	public ExprValueGlobalConstant(GlobalVariable constant) {
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
	/**
	 *  Dots are not allowed in the Evaluator Expression.
	 */
	public String getExpression() {
		return getExpressionTableString().replace(".", "_");
	}

}
