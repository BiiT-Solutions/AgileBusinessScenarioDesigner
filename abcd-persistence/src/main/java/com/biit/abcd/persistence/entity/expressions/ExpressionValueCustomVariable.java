package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines a value as a already defined form custom variable.
 * 
 */
@Entity
@Table(name = "expression_value_custom_variable")
public class ExpressionValueCustomVariable extends ExpressionValueTreeObjectReference {

	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	public ExpressionValueCustomVariable() {
		super();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (variable != null) {
			variable.resetIds();
		}
	}

	public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable) {
		super();
		setReference(reference);
		this.variable = variable;
	}

	public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable, QuestionDateUnit dateUnit) {
		super();
		setReference(reference);
		setUnit(dateUnit);
		this.variable = variable;
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((getReference() != null) && (variable != null)) {
			expressionString += getReference().getName();
			if (getUnit() != null) {
				expressionString += "." + variable.getName() + " (" + getUnit().getAbbreviature() + ")";
			} else {
				expressionString += "." + variable.getName();
			}
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
		expressionString += getReference().getName();
		if (variable != null) {
			expressionString += "." + variable.getName();
		}
		return expressionString;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueCustomVariable copy = new ExpressionValueCustomVariable();
		copy.setReference(getReference());
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueCustomVariable) {
			super.copyData(object);
			ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) object;
			try {
				this.setValue(expressionValueCustomVariable.getValue());
			} catch (NotValidExpressionValue e) {
				throw new NotValidStorableObjectException("Object '" + object
						+ "' is not a valid instance of ExpressionValueCustomVariable.");
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueCustomVariable.");
		}
	}

}
