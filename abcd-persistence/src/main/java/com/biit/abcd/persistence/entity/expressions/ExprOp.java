package com.biit.abcd.persistence.entity.expressions;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;

@Entity
@Table(name = "EXPRESSION_OPERATION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExprOp extends ExprBasic {

	@Enumerated(EnumType.STRING)
	private ExprOpValue currentValue;

	public ExprOp() {
		super();
	}

	public abstract String getValueNullCaption();

	@Override
	public String getExpressionTableString() {
		if (currentValue == null || currentValue == ExprOpValue.NULL) {
			return " " + getValueNullCaption() + " ";
		} else {
			return " " + currentValue.getCaption() + " ";
		}
	}

	public abstract List<ExprOpValue> getAcceptedValues();

	public ExprOpValue getValue() {
		return currentValue;
	}

	public void setValue(ExprOpValue exprOpvalue) throws NotValidOperatorInExpression {
		if (getAcceptedValues().contains(exprOpvalue)) {
			currentValue = exprOpvalue;
		} else {
			throw new NotValidOperatorInExpression("The operator '" + exprOpvalue
					+ "' is not allowed in this expression.");
		}
	}

	@Override
	public String getExpression() {
		return currentValue.toString();
	}

}
