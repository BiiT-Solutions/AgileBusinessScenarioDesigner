package com.biit.abcd.persistence.entity.expressions;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.expressions.interfaces.IExpressionType;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Generic class for creating operators logical and mathematical.
 * 
 */
@Entity
@Table(name = "expression_operator")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExpressionOperator extends Expression implements IExpressionType<AvailableOperator> { 
	private static final long serialVersionUID = 122060488311950177L;
	@Enumerated(EnumType.STRING)
	private AvailableOperator currentValue;

	public ExpressionOperator() {
		super();
	}

	@Override
	public String getRepresentation() {
		if ((currentValue == null) || (currentValue == AvailableOperator.NULL)) {
			return " ";
		} else {
			return " " + currentValue.getCaption() + " ";
		}
	}

	public abstract List<AvailableOperator> getAcceptedValues();

	@Override
	public AvailableOperator getValue() {
		return currentValue;
	}

	/**
	 * Set a value.
	 * 
	 * @param exprOpvalue
	 * @throws NotValidOperatorInExpression
	 *             If this exception is launched, check ALLOWED_OPERATORS of the class.
	 */
	@Override
	public void setValue(AvailableOperator exprOpvalue) throws NotValidOperatorInExpression {
		if (getAcceptedValues().contains(exprOpvalue)) {
			currentValue = exprOpvalue;
		} else {
			throw new NotValidOperatorInExpression("The operator '" + exprOpvalue
					+ "' is not allowed in this expression.");
		}
	}

	@Override
	public String getExpression() {
		return currentValue.getValue();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionOperator) {
			super.copyData(object);
			currentValue = ((ExpressionOperator) object).getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionOperator.");
		}
	}
}
