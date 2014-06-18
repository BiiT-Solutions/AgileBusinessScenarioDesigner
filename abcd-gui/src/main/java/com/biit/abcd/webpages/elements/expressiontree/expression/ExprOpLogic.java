package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprOpLogic extends ExprOp {

	public ExprOpLogic() {
		super();
		getAcceptedValues().add(new ExprOpValue("AND", "AND"));
		getAcceptedValues().add(new ExprOpValue("OR", "OR"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("logic-join");
	}

}
