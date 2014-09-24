package com.biit.abcd.core.drools.prattparser.parselets;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
/**
 * Simple parselet for a named variable like "abc".
 */
public class NameParselet implements PrefixParselet {
	@Override
	public ITreeElement parse(PrattParser parser, ExpressionToken token) {
		return new NameExpression(token);
	}
}
