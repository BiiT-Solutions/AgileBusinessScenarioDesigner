package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_OPERATION_MATH")
public class ExprOpMath extends ExprOp {
	private static final List<ExprOpValue> ALLOWED_OPERATORS = new ArrayList<ExprOpValue>(Arrays.asList(
			ExprOpValue.NULL, ExprOpValue.ASSIGNATION, ExprOpValue.PLUS, ExprOpValue.MINUS, ExprOpValue.MULTIPLICATION,
			ExprOpValue.DIVISION, ExprOpValue.MODULE));

	public ExprOpMath() {
		super();
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("expr-join");
	}

	@Override
	public List<ExprOpValue> getAcceptedValues() {
		return ALLOWED_OPERATORS;
	}

}
