package com.biit.abcd.core.drools.prattparser.expressions;

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

import java.util.List;
import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;

/**
 * A function call like "a(b, c, d)".
 */
public class CallExpression implements ITreeElement {

	private final ITreeElement leftElement;
	private final ExpressionToken function;
	private final List<ITreeElement> args;
	private final String treeElementId;

	public CallExpression(ExpressionToken token, ITreeElement leftElement, List<ITreeElement> args) {
		this.function = token;
		this.leftElement = leftElement;
		this.args = args;
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public ITreeElement getLeftElement() {
		return leftElement;
	}
	
	public ExpressionToken getFunction(){
		return function;
	}

	public List<ITreeElement> getArgs() {
		return this.args;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = null;
		if (leftElement == null) {
			expChain = new ExpressionChain(function.getExpression());
		} else {
			expChain = new ExpressionChain(leftElement.getExpressionChain(), function.getExpression());
		}
		if (!args.isEmpty()) {
			for (ITreeElement treeElem : args) {
				expChain.addExpressions(treeElem.getExpressionChain());
				expChain.addExpressions(new ExpressionSymbol(AvailableSymbol.COMMA));
			}
			// Add the missing right bracket and remove last comma
			expChain.removeLastExpression();
		}
		expChain.addExpressions(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		expChain.setName(this.treeElementId);
		return expChain;
	}
}
