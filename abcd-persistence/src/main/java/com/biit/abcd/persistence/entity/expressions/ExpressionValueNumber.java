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
		return getValueWithoutTrailingZeroes();
	}

	public Double getValue() {
		return new Double(value);
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	protected String getExpression() {
		return getValueWithoutTrailingZeroes();
	}

	private String getValueWithoutTrailingZeroes() {
		Double convertedValue = new Double(value);
		return convertedValue.toString().indexOf(".") < 0 ? convertedValue.toString() : convertedValue.toString()
				.replaceAll("0*$", "").replaceAll("\\.$", "");
	}

}
