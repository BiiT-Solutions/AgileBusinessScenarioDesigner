package com.biit.abcd.core.drools.prattparser.expressions;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A prefix unary arithmetic expression like "!a" or "-b".
 */
public class PrefixExpression implements ITreeElement {

	private final ExpressionTokenType operator;
	private final ITreeElement rightElement;
	private final Expression expression;

	public PrefixExpression(ExpressionToken operator, ITreeElement right) {
		this.operator = operator.getType();
		this.expression = operator.getExpression();
		this.rightElement = right;
	}

	@Override
	public void accept(ITreeElementVisitor visitor) {
		visitor.visit(this);
	}

	public ExpressionTokenType getOperator() {
		return this.operator;
	}

	public ITreeElement getRightElement() {
		return this.rightElement;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.expression);
		expChain.addExpressions(this.rightElement.getExpressionChain());
		return expChain;
	}
}
