package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a value as string. 
 *
 */
@Entity
@Table(name = "EXPRESSION_VALUE_STRING")
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
	public String getExpressionTableString() {
		return value;
	}

	@Override
	protected String getExpression() {
		return value;
	}

}
