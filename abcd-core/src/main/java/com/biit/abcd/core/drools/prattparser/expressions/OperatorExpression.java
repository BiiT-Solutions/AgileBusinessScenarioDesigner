package com.biit.abcd.core.drools.prattparser.expressions;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A binary arithmetic expression like "a + b" or "c ^ d".
 */
public class OperatorExpression implements ITreeElement {

	private final ITreeElement leftElement;
	private final ExpressionTokenType operator;
	private final ITreeElement rightElement;
	private final Expression expression;

	public OperatorExpression(ITreeElement left, ExpressionToken operator, ITreeElement right) {
		this.leftElement = left;
		this.expression = operator.getExpression();
		this.operator = operator.getType();
		this.rightElement = right;
	}

	@Override
	public void accept(ITreeElementVisitor visitor) {
		visitor.visit(this);
	}

	public ITreeElement getLeftElement() {
		return this.leftElement;
	}

	public ExpressionTokenType getOperator() {
		return this.operator;
	}

	public ITreeElement getRightElement() {
		return this.rightElement;
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.leftElement.getExpressionChain());
		expChain.addExpressions(this.expression);
		expChain.addExpressions(this.rightElement.getExpressionChain());
		return expChain;
	}
}
