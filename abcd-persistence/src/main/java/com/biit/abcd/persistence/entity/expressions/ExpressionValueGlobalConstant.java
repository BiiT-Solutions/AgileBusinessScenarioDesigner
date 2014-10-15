package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a value as a already defined Global Constant.
 * 
 */
@Entity
@Table(name = "expression_value_global_variable")
public class ExpressionValueGlobalConstant extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private GlobalVariable constant;

	protected ExpressionValueGlobalConstant() {
		super();
	}

	public ExpressionValueGlobalConstant(GlobalVariable constant) {
		super();
		this.constant = constant;
	}

	@Override
	public String getRepresentation() {
		String expressionString = "";
		if (constant != null) {
			expressionString += constant.getName();
		}
		return expressionString;
	}

	public GlobalVariable getVariable() {
		return constant;
	}

	public void setVariable(GlobalVariable variable) {
		constant = variable;
	}

	@Override
	protected String getExpression() {
		return getRepresentation();
	}

	@Override
	public Object getValue() {
		return getVariable();
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof GlobalVariable)) {
			throw new NotValidExpressionValue("Expected GlobalVariable object in '" + value + "'");
		}
		setVariable((GlobalVariable) value);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(constant);
		innerStorableObjects.addAll(constant.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueGlobalConstant) {
			super.copyData(object);
			ExpressionValueGlobalConstant expressionValueGlobalConstant = (ExpressionValueGlobalConstant) object;
			try {
				this.setValue(expressionValueGlobalConstant.getValue());
			} catch (NotValidExpressionValue e) {
				throw new NotValidStorableObjectException("Object '" + object
						+ "' is not a valid instance of ExpressionValueGlobalConstant.");
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueGlobalConstant.");
		}
	}
}
