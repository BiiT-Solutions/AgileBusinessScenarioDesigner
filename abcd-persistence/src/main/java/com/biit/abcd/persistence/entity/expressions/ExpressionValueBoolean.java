package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

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

	public boolean getValue() {
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

}