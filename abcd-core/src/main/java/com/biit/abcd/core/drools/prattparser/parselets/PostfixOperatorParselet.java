package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
/**
 * Generic infix parselet for an unary arithmetic operator. Parses postfix unary
 * "?" expressions.
 */
public class PostfixOperatorParselet implements InfixParselet {
	private final int mPrecedence;

	public PostfixOperatorParselet(int precedence) {
		this.mPrecedence = precedence;
	}

	@Override
	public ITreeElement parse(Parser parser, ITreeElement left, ExpressionToken token) {
		return new PostfixExpression(left, token);
	}

	@Override
	public int getPrecedence() {
		return this.mPrecedence;
	}
}