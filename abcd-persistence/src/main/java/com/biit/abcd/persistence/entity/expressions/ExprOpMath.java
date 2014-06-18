package com.biit.abcd.persistence.entity.expressions;

public class ExprOpMath extends ExprOp {

	public ExprOpMath() {
		super();
		getAcceptedValues().add(new ExprOpValue("=", "="));
		getAcceptedValues().add(new ExprOpValue("+", "+"));
		getAcceptedValues().add(new ExprOpValue("-", "-"));
		getAcceptedValues().add(new ExprOpValue("*", "*"));
		getAcceptedValues().add(new ExprOpValue("/", "/"));
		getAcceptedValues().add(new ExprOpValue("%", "%"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("expr-join");
	}

}
