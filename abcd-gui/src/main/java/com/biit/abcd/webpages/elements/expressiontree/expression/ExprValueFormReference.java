package com.biit.abcd.webpages.elements.expressiontree.expression;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.TreeObject;

public class ExprValueFormReference extends ExprValue {

	private TreeObject question;
	private CustomVariable variable;

	public ExprValueFormReference(TreeObject question, CustomVariable variable) {
		super();
		this.question = question;
		this.variable = variable;
	}

	@Override
	public String getExpressionTableString() {
		String expressionString = new String();
		expressionString += question;
		if(variable!=null){
			expressionString+="."+variable;
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

}
