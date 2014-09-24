package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
/**
 * Parses parentheses used to group an expression, like "a * (b + c)".
 */
public class GroupParselet implements PrefixParselet {

	@Override
	public ITreeElement parse(PrattParser parser, ExpressionToken token) {
		ITreeElement expression = parser.parseExpression();
		parser.consume(ExpressionTokenType.RIGHT_BRACKET);
		return expression;
	}
}
