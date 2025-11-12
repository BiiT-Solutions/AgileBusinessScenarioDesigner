package com.biit.abcd.core.drools.rules;

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

import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * All the complex rules like 'A and (B or C)' are divided in sets of simple
 * rules like: 'BC = B or C', 'A and BC'.<br>
 * To allow this we create groups of rules so we can know when a rule is part of
 * a more complex one.<br>
 */
public class DroolsRuleGroup extends DroolsRule {
	private static final long serialVersionUID = -1248781299336582512L;
	private String groupCondition = "";
	private String groupAction = "";
	private String conditionExpressionChainId = "";

	public DroolsRuleGroup() {
		super();
	}

	public DroolsRuleGroup(Rule rule) {
		super(rule);
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}

	public String getGroupAction() {
		return groupAction;
	}

	public void setGroupAction(String groupAction) {
		this.groupAction = groupAction;
	}

	public String getConditionExpressionChainId() {
		return conditionExpressionChainId;
	}

	public void setConditionExpressionChainId(String expressionChainId) {
		this.conditionExpressionChainId = expressionChainId;
	}
}
