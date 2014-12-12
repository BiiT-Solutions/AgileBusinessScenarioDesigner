package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.Precedence;
import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;


/**
 * Parses assignment expressions like "a = b". The left side of an assignment
 * expression must be a simple name like "a", and expressions are
 * right-associative. (In other words, "a = b = c" is parsed as "a = (b = c)").
 */
public class AssignParselet implements InfixParselet {

	public ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) throws PrattParserException {
		ITreeElement right = parser.parseExpression(Precedence.ASSIGNMENT - 1);

		if (!(left instanceof NameExpression)) {
			throw new PrattParserException("The left-hand side of an assignment must be a name.");
		}

		return new AssignExpression(((NameExpression) left), right);
	}

	@Override
	public int getPrecedence() {
		return Precedence.ASSIGNMENT;
	}

}