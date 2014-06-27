package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUE_DOUBLE")
public class ExprValueDouble extends ExprValue {

	private double value;

	protected ExprValueDouble() {
		super();
		value = 0d;
	}

	public ExprValueDouble(double value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
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
