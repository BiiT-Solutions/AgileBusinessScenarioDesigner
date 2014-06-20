package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUE_BOOLEAN")
public class ExprValueBoolean extends ExprValue {

	private boolean value;

	protected ExprValueBoolean() {
		super();
		value = true;
	}

	public ExprValueBoolean(boolean value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
		return "" + value;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

}