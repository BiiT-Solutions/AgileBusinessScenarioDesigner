package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;


public interface ITreeElement {

	void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException;
	ExpressionChain getExpressionChain();
}
