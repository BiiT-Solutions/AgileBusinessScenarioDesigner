package com.biit.abcd.persistence.entity.expressions;

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
