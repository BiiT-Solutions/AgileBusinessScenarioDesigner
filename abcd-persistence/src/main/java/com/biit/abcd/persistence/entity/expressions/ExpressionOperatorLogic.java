package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines any logical operator.
 * 
 */
@Entity
@Table(name = "expression_operator_logic")
public class ExpressionOperatorLogic extends ExpressionOperator {
	private static final List<AvailableOperator> ALLOWED_OPERATORS = new ArrayList<AvailableOperator>(Arrays.asList(
			AvailableOperator.NULL, AvailableOperator.AND, AvailableOperator.OR, AvailableOperator.EQUALS,
			AvailableOperator.NOT_EQUALS, AvailableOperator.LESS_EQUALS, AvailableOperator.LESS_THAN,
			AvailableOperator.GREATER_EQUALS, AvailableOperator.GREATER_THAN));

	public ExpressionOperatorLogic() {
		super();
		try {
			setValue(AvailableOperator.NULL);
		} catch (NotValidOperatorInExpression e) {
			// This should never happen
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public ExpressionOperatorLogic(AvailableOperator operator) {
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

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionOperatorLogic) {
			super.copyData(object);
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionOperatorLogic.");
		}
	}

}
