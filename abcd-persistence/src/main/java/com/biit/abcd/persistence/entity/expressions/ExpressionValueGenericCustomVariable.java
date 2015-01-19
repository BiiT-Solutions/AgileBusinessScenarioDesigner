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
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a value as a already defined form custom variable.
 * 
 */
@Entity
@Table(name = "expression_value_generic_custom_variable")
public class ExpressionValueGenericCustomVariable extends ExpressionValueGenericVariable {
	private static final long serialVersionUID = -5189487388656499107L;
	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	public ExpressionValueGenericCustomVariable() {
		super();
	}

	public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable) {
		super();
		setType(type);
		setVariable(variable);
	}

	public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable, boolean editable) {
		super();
		setType(type);
		setVariable(variable);
		setEditable(editable);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (getVariable() != null) {
			getVariable().resetIds();
		}
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((getType() != null) && (getVariable() != null)) {
			expressionString += getType().getExpressionName() + "." + getVariable().getName();
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
		if ((getType() != null) && (getVariable() != null)) {
			expressionString += getType().getExpressionName().replace(".", "_") + "_" + getVariable().getName();
		}
		return expressionString;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (getVariable() != null) {
			innerStorableObjects.add(getVariable());
			innerStorableObjects.addAll(getVariable().getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueGenericCustomVariable) {
			super.copyData(object);
			ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) object;
			this.setVariable(expressionValueGenericCustomVariable.getVariable());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueGenericCustomVariable.");
		}
	}

}
