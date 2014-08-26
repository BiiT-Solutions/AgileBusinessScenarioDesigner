package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.ParseException;
import com.biit.abcd.core.drools.prattparser.Parser;
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

	public ITreeElement parse(Parser parser, ITreeElement left, ExpressionToken token) {
		ITreeElement right = parser.parseExpression(Precedence.ASSIGNMENT - 1);

		if (!(left instanceof NameExpression)) {
			throw new ParseException("The left-hand side of an assignment must be a name.");
		}

		return new AssignExpression(((NameExpression) left), right);
	}

	@Override
	public int getPrecedence() {
		return Precedence.ASSIGNMENT;
	}

}