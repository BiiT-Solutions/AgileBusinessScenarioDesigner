package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;


/**
 * Generic infix parselet for a binary arithmetic operator. The only difference
 * when parsing, "+", "-", "*", "/", and "^" is precedence and associativity, so
 * we can use a single parselet class for all of those.
 */
public class BinaryOperatorParselet implements InfixParselet {

	private final int mPrecedence;
	private final boolean mIsRight;

	public BinaryOperatorParselet(int precedence, boolean isRight) {
		this.mPrecedence = precedence;
		this.mIsRight = isRight;
	}

	public ITreeElement parse(Parser parser, ITreeElement left, ExpressionToken token) {
		// To handle right-associative operators like "^", we allow a slightly
		// lower precedence when parsing the right-hand side. This will let a
		// parselet with the same precedence appear on the right, which will
		// then take *this* parselet's result as its left-hand argument.
		ITreeElement right = parser.parseExpression(this.mPrecedence - (this.mIsRight ? 1 : 0));

		return new OperatorExpression(left, token, right);
	}

	@Override
	public int getPrecedence() {
		return this.mPrecedence;
	}

}