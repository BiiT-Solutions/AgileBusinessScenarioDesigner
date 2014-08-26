package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;


public interface ITreeElement {

	void accept(ITreeElementVisitor visitor);
	ExpressionChain getExpressionChain();
}
