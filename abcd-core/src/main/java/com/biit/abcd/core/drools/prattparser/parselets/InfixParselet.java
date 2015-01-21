package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;

/**
 * One of the two parselet interfaces used by the Pratt parser. An InfixParselet
 * is associated with a token that appears in the middle of the expression it
 * parses. Its parse() method will be called after the left-hand side has been
 * parsed, and it in turn is responsible for parsing everything that comes after
 * the token. This is also used for postfix expressions, in which case it simply
 * doesn't consume any more tokens in its parse() call.
 */

public interface InfixParselet {
	ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) throws PrattParserException;

	int getPrecedence();
}
