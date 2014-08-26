package com.biit.abcd.core.drools.prattparser.expressions;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A simple variable name expression like "abc".
 */
public class NameExpression implements ITreeElement {

	private final String name;
	private final Expression variable;

	public NameExpression(ExpressionToken variable) {
		this.name = variable.toString();
		this.variable = variable.getExpression();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void accept(ITreeElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public ExpressionChain getExpressionChain() {
		return new ExpressionChain(this.variable);
	}
}
