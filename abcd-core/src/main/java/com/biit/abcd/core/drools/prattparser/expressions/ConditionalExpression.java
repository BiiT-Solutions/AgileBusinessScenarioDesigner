package com.biit.abcd.core.drools.prattparser.expressions;

import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A ternary conditional expression like "a ? b : c".
 */
public class ConditionalExpression implements ITreeElement {

	private final ITreeElement condition;
	private final ITreeElement thenArm;
	private final ITreeElement elseArm;

	public ConditionalExpression(ITreeElement condition, ITreeElement thenArm, ITreeElement elseArm) {
		this.condition = condition;
		this.thenArm = thenArm;
		this.elseArm = elseArm;
	}

	@Override
	public void accept(ITreeElementVisitor visitor) {
		visitor.visit(this);
	}

	public ITreeElement getCondition() {
		return this.condition;
	}

	public ITreeElement getThenArm() {
		return this.thenArm;
	}

	public ITreeElement getElseArm() {
		return this.elseArm;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.condition.getExpressionChain());
		expChain.addExpressions(this.thenArm.getExpressionChain());
		expChain.addExpressions(this.elseArm.getExpressionChain());
		return expChain;
	}
}
