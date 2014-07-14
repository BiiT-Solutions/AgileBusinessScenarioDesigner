package com.biit.abcd.persistence.entity.rules;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;

@Entity
@Table(name = "RULE_ACTION_EXPRESSION")
public class ActionExpression extends StorableObject {

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private ExpressionChain expressionChain;

	@Override
	public String toString() {
		return getExpression();
	}

	public boolean undefined() {
		return getExpressionChain() == null;
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}

	public void setExpressionChain(Object expression) throws NotValidExpression {
		if (expression instanceof ExpressionChain) {
			this.expressionChain = (ExpressionChain) expression;
		} else {
			throw new NotValidExpression("Inserted expression of class '" + expression.getClass() + "' is not valid.");
		}
	}

	public String getExpression() {
		if (getExpressionChain() != null) {
			return getExpressionChain().getExpression();
		}
		return "";
	}

}
