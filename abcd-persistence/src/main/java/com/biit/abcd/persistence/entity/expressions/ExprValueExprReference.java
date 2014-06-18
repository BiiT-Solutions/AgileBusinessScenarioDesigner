package com.biit.abcd.persistence.entity.expressions;

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
