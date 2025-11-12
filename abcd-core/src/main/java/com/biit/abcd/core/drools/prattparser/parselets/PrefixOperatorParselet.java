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
