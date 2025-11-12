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

import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A ternary conditional expression like "a ? b : c".
 */
public class ConditionalExpression implements ITreeElement {

	private final ITreeElement condition;
	private final ITreeElement thenArm;
	private final ITreeElement elseArm;
	private final String treeElementId;

	public ConditionalExpression(ITreeElement condition, ITreeElement thenArm, ITreeElement elseArm) {
		this.condition = condition;
		this.thenArm = thenArm;
		this.elseArm = elseArm;
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public ITreeElement getCondition() {
		return this.condition;
	}

	public ITreeElement getThenArm() {
		return this.thenArm;
	}

	public ITreeElement getElseArm() {
		return this.elseArm;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.condition.getExpressionChain());
		expChain.addExpressions(this.thenArm.getExpressionChain());
		expChain.addExpressions(this.elseArm.getExpressionChain());
		expChain.setName(this.treeElementId);
		return expChain;
	}
}
