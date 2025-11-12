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
