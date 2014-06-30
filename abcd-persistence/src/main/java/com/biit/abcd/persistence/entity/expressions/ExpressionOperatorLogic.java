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
	private static final List<AvailableOperators> ALLOWED_OPERATORS = new ArrayList<AvailableOperators>(Arrays.asList(
			AvailableOperators.NULL, AvailableOperators.AND, AvailableOperators.OR, AvailableOperators.EQUALS, AvailableOperators.NOT_EQUALS,
			AvailableOperators.LESS_EQUALS, AvailableOperators.LESS_THAN, AvailableOperators.GREATER_EQUALS, AvailableOperators.GREATER_THAN));

	public ExpressionOperatorLogic() {
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
		return generateNullLabelCaption("logic-join");
	}

	@Override
	public List<AvailableOperators> getAcceptedValues() {
		return ALLOWED_OPERATORS;
	}

}
