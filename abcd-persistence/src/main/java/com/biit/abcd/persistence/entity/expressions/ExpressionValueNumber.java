package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines values as a double.
 * 
 */
@Entity
@Table(name = "expression_value_number")
public class ExpressionValueNumber extends ExpressionValue {

	private double value;

	protected ExpressionValueNumber() {
		super();
		value = 0d;
	}

	public ExpressionValueNumber(double value) {
		super();
		setValue(value);
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
	public String getExpression() {
		return getValueWithoutTrailingZeroes();
	}

	private String getValueWithoutTrailingZeroes() {
		Double convertedValue = new Double(value);
		return convertedValue.toString().indexOf(".") < 0 ? convertedValue.toString() : convertedValue.toString()
				.replaceAll("0*$", "").replaceAll("\\.$", "");
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueNumber copy = new ExpressionValueNumber();
		copy.value = value;
		return copy;
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof Double)) {
			throw new NotValidExpressionValue("Expected Double object in '" + value + "'");
		}
		setValue((Double) value);
	}

}
