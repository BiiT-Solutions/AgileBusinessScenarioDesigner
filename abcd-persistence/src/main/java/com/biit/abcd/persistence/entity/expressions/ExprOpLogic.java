package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_OPERATION_LOGIC")
public class ExprOpLogic extends ExprOp {
	private static final List<ExprOpValue> ALLOWED_OPERATORS = new ArrayList<ExprOpValue>(Arrays.asList(
			ExprOpValue.AND, ExprOpValue.OR, ExprOpValue.NULL, ExprOpValue.EQUALS, ExprOpValue.NOT_EQUALS,
			ExprOpValue.LESS_EQUALS, ExprOpValue.LESS_THAN, ExprOpValue.GREATER_EQUALS, ExprOpValue.GREATER_THAN));

	public ExprOpLogic() {
		super();
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("logic-join");
	}

	@Override
	public List<ExprOpValue> getAcceptedValues() {
		return ALLOWED_OPERATORS;
	}

}
