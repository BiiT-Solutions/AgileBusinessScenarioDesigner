package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines a value as string.
 * 
 */
@Entity
@Table(name = "expression_value_string")
public class ExpressionValueString extends ExpressionValue {

	private String value;

	protected ExpressionValueString() {
		super();
	}

	public ExpressionValueString(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getRepresentation() {
		return value;
	}

	@Override
	protected String getExpression() {
		return value;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueString copy = new ExpressionValueString();
		copy.value = new String(value);
		return copy;
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof String)) {
			throw new NotValidExpressionValue("Expected String object in '" + value + "'");
		}
		setValue((String) value);
	}
}
