package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines boolean values.
 * 
 */
@Entity
@Table(name = "expression_value_boolean")
public class ExpressionValueBoolean extends ExpressionValue {

	private boolean value;

	protected ExpressionValueBoolean() {
		super();
		value = true;
	}

	public ExpressionValueBoolean(boolean value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation() {
		return "" + value;
	}

	// public T getValue() {
	// return value;
	// }

	@Override
	public Object getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public String getExpression() {
		if (value) {
			return "1";
		} else {
			return "0";
		}
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueBoolean copy = new ExpressionValueBoolean();
		copy.value = value;
		return copy;
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof Boolean)) {
			throw new NotValidExpressionValue("Expected Boolean object in '" + value + "'");
		}
		setValue((Boolean) value);
	}

}