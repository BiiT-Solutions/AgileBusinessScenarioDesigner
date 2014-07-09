package com.biit.abcd.persistence.entity.rules;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Expressions;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;

@Entity
@Table(name = "RULE_ACTION_EXPRESSION")
public class ActionExpression extends Action {

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Expressions expression;

	@Override
	public String toString() {
		return getExpressionAsString();
	}

	@Override
	public boolean undefined() {
		return getExpression() == null;
	}

	@Override
	public Expressions getExpression() {
		return expression;
	}

	@Override
	public void setExpression(Object expression) throws NotValidExpression {
		if (expression instanceof Expressions) {
			this.expression = (Expressions) expression;
		} else {
			throw new NotValidExpression("Inserted expression of class '" + expression.getClass() + "' is not valid.");
		}
	}

	@Override
	public String getExpressionAsString() {
		if (getExpression() != null) {
			return getExpression().getExpressionTableString();
		}
		return "";
	}

}
