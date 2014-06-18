package com.biit.abcd.webpages.elements.expressiontree.expression;

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
