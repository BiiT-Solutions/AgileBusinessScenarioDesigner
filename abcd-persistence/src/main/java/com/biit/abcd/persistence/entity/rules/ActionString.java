package com.biit.abcd.persistence.entity.rules;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;

@Entity
@Table(name = "RULE_ACTION_STRING")
public class ActionString extends Action {
	private static final int MAX_CHARACTERS_TO_SHOW = 25;

	private String expression = "";

	public ActionString() {
		try {
			setExpression("");
		} catch (Exception e) {

		}
	}

	@Override
	public String toString() {
		return getExpression().substring(0, Math.min(MAX_CHARACTERS_TO_SHOW, getExpression().length()));
	}

	@Override
	public boolean undefined() {
		return (getExpression() == null || getExpression().length() == 0);
	}

	@Override
	public String getExpression() {
		return expression;
	}

	@Override
	public void setExpression(Object expression) throws NotValidExpression {
		if (expression instanceof String) {
			this.expression = (String) expression;
		} else {
			throw new NotValidExpression("Inserted expression is not valid.");
		}
	}

	@Override
	public String getExpressionAsString() {
		return getExpression();
	}
}
