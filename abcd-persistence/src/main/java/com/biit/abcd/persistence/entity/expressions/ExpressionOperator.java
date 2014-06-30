package com.biit.abcd.persistence.entity.expressions;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;

/**
 * Generic class for creating operators logical and mathematical. 
 *
 */
@Entity
@Table(name = "EXPRESSION_OPERATOR")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExpressionOperator extends Expression {

	@Enumerated(EnumType.STRING)
	private AvailableOperators currentValue;

	public ExpressionOperator() {
		super();
	}

	public abstract String getValueNullCaption();

	@Override
	public String getExpressionTableString() {
		if (currentValue == null || currentValue == AvailableOperators.NULL) {
			return " " + getValueNullCaption() + " ";
		} else {
			return " " + currentValue.getCaption() + " ";
		}
	}

	public abstract List<AvailableOperators> getAcceptedValues();

	public AvailableOperators getValue() {
		return currentValue;
	}

	public void setValue(AvailableOperators exprOpvalue) throws NotValidOperatorInExpression {
		if (getAcceptedValues().contains(exprOpvalue)) {
			currentValue = exprOpvalue;
		} else {
			throw new NotValidOperatorInExpression("The operator '" + exprOpvalue
					+ "' is not allowed in this expression.");
		}
	}

	@Override
	protected String getExpression() {
		return currentValue.toString();
	}

}
