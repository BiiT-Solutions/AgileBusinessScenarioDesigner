package com.biit.abcd.core.drools.rules.validators;

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

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleChecker {

	public static void checkRule(DiagramElement node, Rule rule, String ruleName) throws PrattParserException, InvalidExpressionException,
			NotCompatibleTypeException {
		if ((rule.getConditions() != null) && (rule.getConditions().getExpressions() != null) && !(rule.getConditions().getExpressions().isEmpty())
				&& (rule.getActions() != null) && (rule.getActions().getExpressions() != null) && !(rule.getActions().getExpressions().isEmpty())) {
			try {
				ExpressionValidator.validateRule(rule);
			} catch (NotCompatibleTypeException ncte) {
				if (node != null) {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "' at diagram node '" + node.getText() + "'.", ncte);
				} else {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "'.", ncte);
				}
			}

		} else if ((rule.getActions() == null) || (rule.getActions().getExpressions() == null) || (rule.getActions().getExpressions().isEmpty())) {
			// Some rules don't have actions defined by the user (like the
			// group rules)
			try {
				ExpressionValidator.validateConditions(rule.getConditions());
			} catch (NotCompatibleTypeException ncte) {
				if (node != null) {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "' at diagram node '" + node.getText() + "'.", ncte);
				} else {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "'.", ncte);
				}
			}
		} else {
			try {
				ExpressionValidator.validateActions(rule.getActions());
			} catch (NotCompatibleTypeException ncte) {
				if (node != null) {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "' at diagram node '" + node.getText() + "'.", ncte);
				} else {
					throw new NotCompatibleTypeException("Error parsing '" + rule + "'.", ncte);
				}
			}
		}
	}

	/**
	 * Checks if the rule is valid<br>
	 * Invalid rules throw an exception
	 *
	 * @throws NotCompatibleTypeException
	 * @throws InvalidExpressionException
	 * @throws PrattParserException
	 */

	public static void checkRule(DiagramElement node, Rule rule) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		checkRule(node, rule, null);
	}
}
