package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a value as a already defined Global Constant.
 * 
 */
@Entity
@Table(name = "expression_value_global_variable")
public class ExpressionValueGlobalVariable extends ExpressionValue<GlobalVariable> {
	private static final long serialVersionUID = 3063006330916018596L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="global_variable")
	private GlobalVariable globalVariable;

	protected ExpressionValueGlobalVariable() {
		super();
	}

	public ExpressionValueGlobalVariable(GlobalVariable globalVariable) {
		super();
		this.globalVariable = globalVariable;
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		String expressionString = "";
		if (globalVariable != null) {
			expressionString += globalVariable.getName();
		}
		return expressionString;
	}

	public void setVariable(GlobalVariable variable) {
		globalVariable = variable;
	}

	@Override
	protected String getExpression() {
		return getRepresentation(false);
	}

	@Override
	public GlobalVariable getValue() {
		return globalVariable;
	}

	@Override
	public void setValue(GlobalVariable globalVariable) {
		this.globalVariable = globalVariable;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (globalVariable != null) {
			innerStorableObjects.add(globalVariable);
			innerStorableObjects.addAll(globalVariable.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueGlobalVariable) {
			super.copyData(object);
			ExpressionValueGlobalVariable expressionValueGlobalConstant = (ExpressionValueGlobalVariable) object;
			this.setValue(expressionValueGlobalConstant.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueGlobalConstant.");
		}
	}
}
