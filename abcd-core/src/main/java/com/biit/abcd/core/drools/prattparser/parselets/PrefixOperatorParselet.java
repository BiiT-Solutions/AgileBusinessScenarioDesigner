package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;


/**
 * Generic prefix parselet for an unary arithmetic operator. Parses prefix unary
 * "-", "+", "~", and "!" expressions.
 */
public class PrefixOperatorParselet implements PrefixParselet {
	private final int mPrecedence;

	public PrefixOperatorParselet(int precedence) {
		this.mPrecedence = precedence;
	}

	@Override
	public ITreeElement parse(PrattParser parser, ExpressionToken token) throws PrattParserException {
		// To handle right-associative operators like "^", we allow a slightly
		// lower precedence when parsing the right-hand side. This will let a
		// parselet with the same precedence appear on the right, which will
		// then
		// take *this* parselet's result as its left-hand argument.
		ITreeElement right = parser.parseExpression(this.mPrecedence);

		return new PrefixExpression(token, right);
	}

	public int getPrecedence() {
		return this.mPrecedence;
	}

}