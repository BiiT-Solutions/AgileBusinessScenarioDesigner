package com.biit.abcd.persistence.entity.rules;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;

@Entity
@Table(name = "RULE_ANSWER_EXPRESSION")
public class AnswerExpression extends ExpressionValueTreeObjectReference {

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private ExpressionChain expressionChain;

	public boolean undefined() {
		return getExpressionChain() == null;
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}

	public void setExpressionChain(Object expression) throws NotValidExpression {
		if(expression == null){
			this.expressionChain = null;
		}
		else if (expression instanceof ExpressionChain) {
			this.expressionChain = (ExpressionChain) expression;
		} else {
			throw new NotValidExpression("Inserted expression of class '" + expression.getClass() + "' is not valid.");
		}
	}

	@Override
	public String getRepresentation() {
		return toString();
	}

	@Override
	public String toString() {
		if(getReference() != null){
			return "" + getReference();
		}
		else if (getExpressionChain() != null) {
			return getExpressionChain().getRepresentation();
		}
		return "null";
	}

	@Override
	protected String getExpression() {
		if (getExpressionChain() != null) {
			return getExpressionChain().getExpression();
		}
		return "null";
	}
}
