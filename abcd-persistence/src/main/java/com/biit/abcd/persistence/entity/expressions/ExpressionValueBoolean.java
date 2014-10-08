package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines boolean values.
 * 
 */
@Entity
@Table(name = "expression_value_boolean")
public class ExpressionValueBoolean extends ExpressionValue {

	private boolean value;

	protected ExpressionValueBoolean() {
		super();
		value = true;
	}

	public ExpressionValueBoolean(boolean value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation() {
		return "" + value;
	}

	// public T getValue() {
	// return value;
	// }

	@Override
	public Object getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public String getExpression() {
		if (value) {
			return "1";
		} else {
			return "0";
		}
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueBoolean copy = new ExpressionValueBoolean();
		copy.value = value;
		return copy;
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof Boolean)) {
			throw new NotValidExpressionValue("Expected Boolean object in '" + value + "'");
		}
		setValue((Boolean) value);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueBoolean) {
			super.copyData(object);
			ExpressionValueBoolean expressionValueBoolean = (ExpressionValueBoolean) object;
			try {
				this.setValue(expressionValueBoolean.getValue());
			} catch (NotValidExpressionValue e) {
				throw new NotValidStorableObjectException("Object '" + object
						+ "' has an invalid ExpressionValueBoolean.");
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueBoolean.");
		}
	}

}