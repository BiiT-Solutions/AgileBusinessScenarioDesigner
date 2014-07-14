package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;

/**
 * Defines any logical operator.
 *
 */
@Entity
@Table(name = "EXPRESSION_OPERATOR_LOGIC")
public class ExpressionOperatorLogic extends ExpressionOperator {
	private static final List<AvailableOperator> ALLOWED_OPERATORS = new ArrayList<AvailableOperator>(Arrays.asList(
			AvailableOperator.NULL, AvailableOperator.AND, AvailableOperator.OR, AvailableOperator.EQUALS, AvailableOperator.NOT_EQUALS,
			AvailableOperator.LESS_EQUALS, AvailableOperator.LESS_THAN, AvailableOperator.GREATER_EQUALS, AvailableOperator.GREATER_THAN));

	public ExpressionOperatorLogic() {
		super();
		try {
			setValue(AvailableOperator.NULL);
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
