package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprOpLogic extends ExprOp {

	public ExprOpLogic() {
		getAcceptedValues().add(new JointValue("AND", "AND"));
		getAcceptedValues().add(new JointValue("OR", "OR"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("logic-join");
	}

}
