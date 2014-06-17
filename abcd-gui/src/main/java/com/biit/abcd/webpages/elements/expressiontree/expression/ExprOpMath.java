package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprOpMath extends ExprOp {

	public ExprOpMath() {
		getAcceptedValues().add(new JointValue("=", "="));
		getAcceptedValues().add(new JointValue("+", "+"));
		getAcceptedValues().add(new JointValue("-", "-"));
		getAcceptedValues().add(new JointValue("*", "*"));
		getAcceptedValues().add(new JointValue("/", "/"));
		getAcceptedValues().add(new JointValue("%", "%"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("expr-join");
	}

}
