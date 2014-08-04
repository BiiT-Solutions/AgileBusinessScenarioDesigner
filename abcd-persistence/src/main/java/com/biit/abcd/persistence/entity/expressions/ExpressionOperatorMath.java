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
	private static final List<AvailableOperator> ALLOWED_OPERATORS = new ArrayList<AvailableOperator>(Arrays.asList(
			AvailableOperator.NULL, AvailableOperator.ASSIGNATION, AvailableOperator.PLUS, AvailableOperator.MINUS,
			AvailableOperator.MULTIPLICATION, AvailableOperator.DIVISION, AvailableOperator.MODULE,
			AvailableOperator.POW));

	public ExpressionOperatorMath() {
		super();
		try {
			setValue(AvailableOperator.NULL);
		} catch (NotValidOperatorInExpression e) {
			// This should never happen
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public ExpressionOperatorMath(AvailableOperator operator) {
		super();
		try {
			setValue(operator);
		} catch (NotValidOperatorInExpression e) {
			// This should never happen
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public List<AvailableOperator> getAcceptedValues() {
		return ALLOWED_OPERATORS;
	}

}
