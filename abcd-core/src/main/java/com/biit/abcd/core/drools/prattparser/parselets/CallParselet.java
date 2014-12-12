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
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;

/**
 * Parselet to parse a function call like "a(b, c, d)".
 */
public class CallParselet implements InfixParselet {

	@Override
	public ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) throws PrattParserException {
		// When the IF function is used a dummy variable is introduces and have
		// to be removed before returning the parsed expression
		if ((token.getExpression() instanceof ExpressionFunction)
				&& ((ExpressionFunction) token.getExpression()).getValue().equals(AvailableFunction.IF)) {
			left = null;
		}

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