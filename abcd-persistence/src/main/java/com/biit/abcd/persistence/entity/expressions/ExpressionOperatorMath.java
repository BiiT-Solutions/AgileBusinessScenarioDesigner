package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;

/**
 * Defines any mathematical operator. 
 */
@Entity
@Table(name = "EXPRESSION_OPERATOR_MATH")
public class ExpressionOperatorMath extends ExpressionOperator {
	private static final List<AvailableOperators> ALLOWED_OPERATORS = new ArrayList<AvailableOperators>(Arrays.asList(
			AvailableOperators.NULL, AvailableOperators.ASSIGNATION, AvailableOperators.PLUS, AvailableOperators.MINUS, AvailableOperators.MULTIPLICATION,
			AvailableOperators.DIVISION, AvailableOperators.MODULE));

	public ExpressionOperatorMath() {
		super();
		try {
			setValue(AvailableOperators.NULL);
		} catch (NotValidOperatorInExpression e) {
			// This should never happen
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("expr-join");
	}

	@Override
	public List<AvailableOperators> getAcceptedValues() {
		return ALLOWED_OPERATORS;
	}

}
