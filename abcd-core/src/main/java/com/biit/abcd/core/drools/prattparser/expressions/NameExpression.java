package com.biit.abcd.core.drools.prattparser.expressions;

import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A simple variable name expression like "abc".
 */
public class NameExpression implements ITreeElement {

	private final Expression expression;
	private final String name;
	private final String treeElementId;

	public NameExpression(ExpressionToken variable) {
		this.name = variable.toString();
		this.expression = variable.getExpression();
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(getExpression());
		expChain.setName(this.treeElementId);
		return expChain;
	}
	
	public String getName() {
		return this.name;
	}
}
