package com.biit.abcd.core.drools.prattparser.parselets;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
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

	public ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) throws PrattParserException {
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
