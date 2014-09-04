package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines a value as another existing expression.
 * 
 */
@Entity
@Table(name = "expression_value_expression_reference")
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
	 * This Generate copy assumes that the expression is not a "live reference" to the form, but just a expression
	 * stored on database only, thus we clone it also.
	 */
	@Override
	public Expression generateCopy() {
		ExpressionValueExpressionReference copy = new ExpressionValueExpressionReference();
		copy.value = value.generateCopy();
		return copy;
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof Expression)) {
			throw new NotValidExpressionValue("Expected Expression object in '" + value + "'");
		}
		setValue((Expression) value);
	}

}
