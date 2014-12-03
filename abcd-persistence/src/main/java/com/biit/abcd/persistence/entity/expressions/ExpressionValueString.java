package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a value as string.
 * 
 */
@Entity
@Table(name = "expression_value_string")
public class ExpressionValueString extends ExpressionValue {

	@Column(columnDefinition = "TEXT")
	private String value;

	protected ExpressionValueString() {
		super();
	}

	public ExpressionValueString(String value) {
		super();
		setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getRepresentation() {
		return getValue();
	}

	@Override
	protected String getExpression() {
		return getValue();
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof String)) {
			throw new NotValidExpressionValue("Expected String object in '" + value + "'");
		}
		setValue((String) value);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueString) {
			super.copyData(object);
			ExpressionValueString expressionValueString = (ExpressionValueString) object;
			this.setValue(expressionValueString.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueString.");
		}
	}
}
