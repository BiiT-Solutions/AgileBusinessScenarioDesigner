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

/**
 * Defines any mathematical operator.
 */
@Entity
@Table(name = "expression_operator_math")
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

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

}
