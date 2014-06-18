package com.biit.abcd.persistence.entity.expressions;

public class ExprValueDouble extends ExprValue{
	
	private double value;
	
	public ExprValueDouble(double value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
		return ""+value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
