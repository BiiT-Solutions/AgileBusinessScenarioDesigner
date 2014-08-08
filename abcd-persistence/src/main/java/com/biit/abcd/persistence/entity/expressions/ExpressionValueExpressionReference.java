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
		setValue(value);
	}

	@Override
	public String getRepresentation() {
		if (value == null) {
			return "";
		} else {
			return value.getRepresentation();
		}
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	@Override
	public String getExpression() {
		return value.getExpression();
	}

	/**
	 * This Generate copy assumes that the expression is not a "live reference"
	 * to the form, but just a expression stored on database only, thus we clone
	 * it also.
	 */
	@Override
	public Expression generateCopy() {
		ExpressionValueExpressionReference copy = new ExpressionValueExpressionReference();
		copy.value = value.generateCopy();
		return copy;
	}

}
