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
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
/**
 * Generic infix parselet for an unary arithmetic operator. Parses postfix unary
 * "?" expressions.
 */
public class PostfixOperatorParselet implements InfixParselet {
	private final int mPrecedence;

	public PostfixOperatorParselet(int precedence) {
		this.mPrecedence = precedence;
	}

	@Override
	public ITreeElement parse(PrattParser parser, ITreeElement left, ExpressionToken token) {
		return new PostfixExpression(left, token);
	}

	@Override
	public int getPrecedence() {
		return this.mPrecedence;
	}
}
