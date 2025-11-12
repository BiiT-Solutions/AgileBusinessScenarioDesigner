package com.biit.abcd.core.drools.prattparser.visitor;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.GroupExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * This visitor checks if the rule is a complex rule (i.e. has AND, OR or NOT
 * expressions).<br>
 * If it is a complex rule the visitor separates the rule conditions.
 */
public class TreeElementGroupConditionFinderVisitor implements ITreeElementVisitor {

	private List<ExpressionChain> conditions;

	public TreeElementGroupConditionFinderVisitor() {
		setConditions(new ArrayList<ExpressionChain>());
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		assign.getRightElement().accept(this);
		getConditions().add(assign.getExpressionChain());
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getLeftElement().accept(this);
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
		}
		getConditions().add(call.getExpressionChain());
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		condition.getCondition().accept(this);
		condition.getThenArm().accept(this);
		condition.getElseArm().accept(this);
	}

	@Override
	public void visit(NameExpression name) {
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		operator.getLeftElement().accept(this);
		if (!operator.getOperator().equals(ExpressionTokenType.OR)
				&& !operator.getOperator().equals(ExpressionTokenType.AND)) {
			getConditions().add(operator.getExpressionChain());
		}
		operator.getRightElement().accept(this);
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		postfix.getLeftElement().accept(this);
		getConditions().add(postfix.getExpressionChain());
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		if (!prefix.getOperator().equals(ExpressionTokenType.NOT)) {
			getConditions().add(prefix.getExpressionChain());
		}
		prefix.getRightElement().accept(this);
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		group.getElement().accept(this);
	}

	public void setConditions(List<ExpressionChain> conditions) {
		this.conditions = conditions;
	}

	public List<ExpressionChain> getConditions() {
		return this.conditions;
	}
}
