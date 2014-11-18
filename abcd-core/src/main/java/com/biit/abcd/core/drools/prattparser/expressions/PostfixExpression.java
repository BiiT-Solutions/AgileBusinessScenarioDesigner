package com.biit.abcd.core.drools.prattparser.expressions;

import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A postfix unary arithmetic expression like "a!".
 */
public class PostfixExpression implements ITreeElement {

	private final ITreeElement leftElement;
	private final ExpressionTokenType operator;
	private final Expression expression;
	private final String treeElementId;

	public PostfixExpression(ITreeElement left, ExpressionToken operator) {
		this.operator = operator.getType();
		this.leftElement = left;
		this.expression = operator.getExpression();
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public ITreeElement getLeftElement() {
		return this.leftElement;
	}

	public ExpressionTokenType getOperator() {
		return this.operator;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.leftElement.getExpressionChain());
		expChain.addExpressions(this.expression);
		expChain.setName(this.treeElementId);
		return expChain;
	}
}
