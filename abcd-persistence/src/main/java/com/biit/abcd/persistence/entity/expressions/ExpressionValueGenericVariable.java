package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "expression_value_generic_variable")
public class ExpressionValueGenericVariable extends ExpressionValue {

	@Enumerated(EnumType.STRING)
	private GenericTreeObjectType type;

	public ExpressionValueGenericVariable() {
		super();
	}

	public ExpressionValueGenericVariable(GenericTreeObjectType variable) {
		super();
		setType(variable);
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((getType() != null)) {
			expressionString += getType().getExpressionName();
		}
		return expressionString;
	}

	public GenericTreeObjectType getType() {
		return type;
	}

	public void setType(GenericTreeObjectType type) {
		this.type = type;
	}

	@Override
	protected String getExpression() {
		String expressionString = new String();
		if (getType() != null) {
			expressionString += getType().getExpressionName().replace(".", "_");
		}
		return expressionString;
	}

	@Override
	public Object getValue() {
		return getType();
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof GenericTreeObjectType)) {
			throw new NotValidExpressionValue("Expected GenericTreeObjectType object in '" + value + "'");
		}
		setType((GenericTreeObjectType) value);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueGenericVariable) {
			super.copyData(object);
			ExpressionValueGenericVariable expressionValueGenericVariable = (ExpressionValueGenericVariable) object;
			setType(expressionValueGenericVariable.getType());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueGenericVariable.");
		}
	}

}
