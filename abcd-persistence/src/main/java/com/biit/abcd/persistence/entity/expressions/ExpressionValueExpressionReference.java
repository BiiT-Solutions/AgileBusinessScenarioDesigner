package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Defines a value as another existing expression.
 * 
 */
@Entity
@Table(name = "EXPRESSION_VALUE_EXPRESSION_REFERENCE")
public class ExpressionValueExpressionReference extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private Expression value;

	protected ExpressionValueExpressionReference() {
		super();
	}

	public ExpressionValueExpressionReference(Expression value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getRepresentation() {
		return value.getRepresentation();
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	@Override
	protected String getExpression() {
		return value.getExpression();
	}

}
