package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;

@Entity
@Table(name = "EXPRESSION_OPERATION_MATH")
public class ExprOpMath extends ExprOp {
	private static final List<ExprOpValue> ALLOWED_OPERATORS = new ArrayList<ExprOpValue>(Arrays.asList(
			ExprOpValue.NULL, ExprOpValue.ASSIGNATION, ExprOpValue.PLUS, ExprOpValue.MINUS, ExprOpValue.MULTIPLICATION,
			ExprOpValue.DIVISION, ExprOpValue.MODULE));

	public ExprOpMath() {
		super();
		try {
			setValue(ExprOpValue.NULL);
		} catch (NotValidOperatorInExpression e) {
			//This should never happen
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
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
