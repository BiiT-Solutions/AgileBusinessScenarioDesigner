package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.TreeObject;

/**
 * Defines a value as a already defined form custom variable.
 *
 */
@Entity
@Table(name = "EXPRESSION_VALUE_FORM_CUSTOM_VARIABLE")
public class ExpressionValueFormCustomVariable extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject question;
	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	protected ExpressionValueFormCustomVariable() {
		super();
	}

	public ExpressionValueFormCustomVariable(TreeObject question, CustomVariable variable) {
		super();
		this.question = question;
		this.variable = variable;
	}

	@Override
	public String getRepresentation() {
		String expressionString = new String();
		expressionString += question.getName();
		if (variable != null) {
			expressionString += "." + variable.getName();
		}
		return expressionString;
	}

	public CustomVariable getVariable() {
		return variable;
	}

	public void setVariable(CustomVariable variable) {
		this.variable = variable;
	}

	public TreeObject getQuestion() {
		return question;
	}

	public void setQuestion(TreeObject question) {
		this.question = question;
	}

	@Override
	protected String getExpression() {
		return getRepresentation();
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueFormCustomVariable copy = new ExpressionValueFormCustomVariable();
		copy.question = question;
		copy.variable = variable;
		return copy;
	}

}
