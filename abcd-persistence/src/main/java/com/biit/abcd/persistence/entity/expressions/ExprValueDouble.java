package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUE_DOUBLE")
public class ExprValueDouble extends ExprValue {

	private double value;

	protected ExprValueDouble() {
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

}
