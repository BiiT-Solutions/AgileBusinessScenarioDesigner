package com.biit.abcd.core.drools.prattparser.parselets;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.Precedence;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;

/**
 * Parselet to parse a function call like "a(b, c, d)".
 */
public class CallParselet implements InfixParselet {

	@Override
	public ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) throws PrattParserException {
		// Parse the comma-separated arguments until we hit, ")".
		List<ITreeElement> args = new ArrayList<ITreeElement>();

		// There may be no arguments at all.
		if (!parser.match(ExpressionTokenType.RIGHT_BRACKET)) {
			do {
				ITreeElement te = parser.parseExpression();
				args.add(te);
			} while (parser.match(ExpressionTokenType.COMMA));
			parser.consume(ExpressionTokenType.RIGHT_BRACKET);
		}

		return new CallExpression(token, left, args);
	}

	@Override
	public int getPrecedence() {
		return Precedence.CALL;
	}
}