package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines boolean values.
 * 
 */
@Entity
@Table(name = "expression_value_boolean")
public class ExpressionValueBoolean extends ExpressionValue<Boolean> {
	private static final long serialVersionUID = 4438705376703075628L;
	private Boolean value;

	protected ExpressionValueBoolean() {
		super();
		value = true;
	}

	public ExpressionValueBoolean(Boolean value) {
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
	public Boolean getValue() {
		return value;
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
	public void setValue(Boolean value) {
		this.value = value;
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
			this.setValue(expressionValueBoolean.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueBoolean.");
		}
	}

}