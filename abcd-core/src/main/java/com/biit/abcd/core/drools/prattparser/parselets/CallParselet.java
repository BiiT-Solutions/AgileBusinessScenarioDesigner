package com.biit.abcd.core.drools.prattparser.parselets;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.Precedence;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;

/**
 * Parselet to parse a function call like "a(b, c, d)".
 */
public class CallParselet implements InfixParselet {

	public ITreeElement parse(Parser parser, ITreeElement left, ExpressionToken token) {
		// Parse the comma-separated arguments until we hit, ")".
		List<ITreeElement> args = new ArrayList<ITreeElement>();

		// There may be no arguments at all.
		if (!parser.match(ExpressionTokenType.RIGHT_BRACKET)) {
			do {
				args.add(parser.parseExpression());
			} while (parser.match(ExpressionTokenType.COMMA));
			parser.consume(ExpressionTokenType.RIGHT_BRACKET);
		}

		return new CallExpression(left, args);
	}

	@Override
	public int getPrecedence() {
		return Precedence.CALL;
	}
}