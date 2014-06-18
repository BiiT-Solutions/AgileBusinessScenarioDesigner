package com.biit.abcd.webpages.elements.expressiontree.expression;

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
