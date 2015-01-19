package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.interfaces.IExpressionType;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a special symbol as '(', ')', or ','
 * 
 */
@Entity
@Table(name = "expression_symbol")
public class ExpressionSymbol extends Expression implements IExpressionType<AvailableSymbol> {
	private static final long serialVersionUID = 3876333374228054372L;
	@Enumerated(EnumType.STRING)
	private AvailableSymbol value;

	public ExpressionSymbol() {
		super();
	}

	public ExpressionSymbol(AvailableSymbol symbol) {
		super();
		setValue(symbol);
	}

	@Override
	public String getRepresentation() {
		if (value == null) {
			return "";
		} else {
			return value.getValue();
		}
	}

	@Override
	public void setValue(AvailableSymbol value) {
		this.value = value;
	}

	@Override
	public AvailableSymbol getValue() {
		return value;
	}

	@Override
	protected String getExpression() {
		if (value.equals(AvailableSymbol.PILCROW)) {
			return "";
		}
		return getRepresentation();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionSymbol) {
			super.copyData(object);
			ExpressionSymbol expressionSymbol = (ExpressionSymbol) object;
			value = expressionSymbol.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionSymbol.");
		}
	}
}
