package com.biit.abcd.core.drools.prattparser.expressions;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A binary arithmetic expression like "a + b" or "c ^ d".
 */
public class OperatorExpression implements ITreeElement {

	private final ITreeElement leftElement;
	private final ITreeElement rightElement;
	private final ExpressionTokenType operatorTokenType;
	private final Expression operatorExpression;

	public OperatorExpression(ITreeElement left, ExpressionToken operator, ITreeElement right) {
		this.leftElement = left;
		this.rightElement = right;
		this.operatorExpression = operator.getExpression();
		this.operatorTokenType = operator.getType();
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public ITreeElement getLeftElement() {
		return this.leftElement;
	}

	public ExpressionTokenType getOperator() {
		return this.operatorTokenType;
	}

	public ITreeElement getRightElement() {
		return this.rightElement;
	}

	public Expression getExpression() {
		return this.operatorExpression;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.leftElement.getExpressionChain());
		expChain.addExpressions(this.operatorExpression);
		expChain.addExpressions(this.rightElement.getExpressionChain());
		return expChain;
	}
}
