package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.persistence.entity.StorableObject;

/**
 * Defines a value as a already defined form custom variable.
 * 
 */
@Entity
@Table(name = "expression_value_generic_custom_variable")
public class ExpressionValueGenericCustomVariable extends ExpressionValueGenericVariable {

	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	public ExpressionValueGenericCustomVariable() {
		super();
	}

	public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable) {
		super();
		setType(type);
		this.variable = variable;
	}

	public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable, boolean editable) {
		super();
		setType(type);
		this.variable = variable;
		setEditable(editable);
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((getType() != null) && (variable != null)) {
			expressionString += getType().getExpressionName() + "." + variable.getName();
		}
		return expressionString;
	}

	public CustomVariable getVariable() {
		return variable;
	}

	public void setVariable(CustomVariable variable) {
		this.variable = variable;
	}

	@Override
	protected String getExpression() {
		String expressionString = new String();
		if ((getType() != null) && (variable != null)) {
			expressionString += getType().getExpressionName().replace(".", "_") + "_" + variable.getName();
		}
		return expressionString;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueGenericCustomVariable copy = new ExpressionValueGenericCustomVariable();
		copy.setType(getType());
		copy.variable = variable;
		copy.setEditable(isEditable());
		return copy;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(variable);
		innerStorableObjects.addAll(variable.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

}
