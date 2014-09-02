package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.GenericTreeObjectType;

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
		type = variable;
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		if ((type != null)) {
			expressionString += type.getExpressionName();
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
		if (type != null) {
			expressionString += type.getExpressionName().replace(".", "_");
		}
		return expressionString;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueGenericVariable copy = new ExpressionValueGenericVariable();
		copy.type = type;
		copy.setEditable(isEditable());
		return copy;
	}

	@Override
	public Object getValue() {
		return getType();
	}

}
