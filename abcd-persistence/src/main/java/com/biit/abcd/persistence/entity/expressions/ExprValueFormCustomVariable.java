package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.TreeObject;

@Entity
@Table(name = "EXPRESSION_VALUE_FORM_REFERENCE")
public class ExprValueFormCustomVariable extends ExprValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject question;
	@ManyToOne(fetch = FetchType.EAGER)
	private CustomVariable variable;

	protected ExprValueFormCustomVariable() {
		super();
	}

	public ExprValueFormCustomVariable(TreeObject question, CustomVariable variable) {
		super();
		this.question = question;
		this.variable = variable;
	}

	@Override
	public String getExpressionTableString() {
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
	/**
	 *  Dots are not allowed in the Evaluator Expression.
	 */
	public String getExpression() {
		return getExpressionTableString().replace(".", "_");
	}

}
