package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines values as a double.
 *
 */
@Entity
@Table(name = "EXPRESSION_VALUE_NUMBER")
public class ExpressionValueNumber extends ExpressionValue {

	private double value;

	protected ExpressionValueNumber() {
		super();
		value = 0d;
	}

	public ExpressionValueNumber(double value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getRepresentation() {
		return "" + value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	protected String getExpression() {
		return new Double(value).toString();
	}

}
