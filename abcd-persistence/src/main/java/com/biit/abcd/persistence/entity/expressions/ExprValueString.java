package com.biit.abcd.persistence.entity.expressions;

public class ExprValueString extends ExprValue {

	private String value;
	
	public ExprValueString(String value) {
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

}
