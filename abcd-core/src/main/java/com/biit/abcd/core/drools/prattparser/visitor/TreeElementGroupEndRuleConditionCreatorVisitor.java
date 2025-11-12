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
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;

/**
 * This visitor is in charge of creating the special group rule that is a
 * composition of the simple rules generated before.<br>
 * It also adds conditions to avoid that the rule is fired several times.<br>
 * Is used in combination with the visitors
 * 'TreeElementGroupConditionFinderVisitor' and
 * 'TreeElementGroupEndConditionFinderVisitor'.
 */
public class TreeElementGroupEndRuleConditionCreatorVisitor implements ITreeElementVisitor {

	private ExpressionChain completeExpression;

	public TreeElementGroupEndRuleConditionCreatorVisitor() {
		setCompleteExpression(new ExpressionChain());
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		assign.getRightElement().accept(this);
		String droolsCondition = "FiredRule( getRuleName() == '" + assign.getExpressionChain().getName().toString()
				+ "')\n";
		getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getLeftElement().accept(this);
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
		}
		String droolsCondition = "FiredRule( getRuleName() == '" + call.getExpressionChain().getName().toString()
				+ "')\n";
		getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		condition.getCondition().accept(this);
		condition.getThenArm().accept(this);
		condition.getElseArm().accept(this);
		String droolsCondition = "FiredRule( getRuleName() == '" + condition.getExpressionChain().getName().toString()
				+ "')\n";
		getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
	}

	@Override
	public void visit(NameExpression name) {
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		if (operator.getOperator().equals(ExpressionTokenType.AND)
				|| operator.getOperator().equals(ExpressionTokenType.OR)) {
			getCompleteExpression().addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		}

		operator.getLeftElement().accept(this);

		if (operator.getOperator().equals(ExpressionTokenType.OR)) {
			getCompleteExpression().addExpression(new ExpressionValueString("or"));

		} else if (operator.getOperator().equals(ExpressionTokenType.AND)) {
			getCompleteExpression().addExpression(new ExpressionValueString("and"));

		} else {
			String droolsCondition = "FiredRule( getRuleName() == '"
					+ operator.getExpressionChain().getName().toString() + "')\n";
			getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
		}

		operator.getRightElement().accept(this);

		if (operator.getOperator().equals(ExpressionTokenType.AND)
				|| operator.getOperator().equals(ExpressionTokenType.OR)) {
			getCompleteExpression().addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		}
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		postfix.getLeftElement().accept(this);
		String droolsCondition = "FiredRule( getRuleName() == '" + postfix.getExpressionChain().getName().toString()
				+ "')\n";
		getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		if (prefix.getOperator().equals(ExpressionTokenType.NOT)) {
			getCompleteExpression().addExpression(new ExpressionValueString("not( "));
		} else {
			String droolsCondition = "FiredRule( getRuleName() == '" + prefix.getExpressionChain().getName()
					+ "')\n";
			getCompleteExpression().addExpression(new ExpressionValueString(droolsCondition));
		}
		prefix.getRightElement().accept(this);
		if (prefix.getOperator().equals(ExpressionTokenType.NOT)) {
			getCompleteExpression().addExpression(new ExpressionValueString(" )\n"));
		}
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		group.getElement().accept(this);
	}

	public ExpressionChain getCompleteExpression() {
		return completeExpression;
	}

	public void setCompleteExpression(ExpressionChain completeExpression) {
		this.completeExpression = completeExpression;
	}
}
