package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprValueExprReference extends ExprValue{
	
	private ExprBasic value;
	
	public ExprValueExprReference(ExprBasic value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
		return value.getExpressionTableString();
	}

	public ExprBasic getValue() {
		return value;
	}

	public void setValue(ExprBasic value) {
		this.value = value;
	}

}
