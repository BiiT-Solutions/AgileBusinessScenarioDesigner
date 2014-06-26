package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUE_STRING")
public class ExprValueString extends ExprValue {

	private String value;

	protected ExprValueString() {
		super();
	}

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

	@Override
	public String getExpression() {
		return value;
	}

}
