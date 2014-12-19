package com.biit.abcd.core.drools.rules.validators;

import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleChecker {

	public static void checkRule(Rule rule, String ruleName) throws InvalidRuleException {
		try {
			if ((rule.getConditions() != null) && (rule.getConditions().getExpressions() != null)
					&& !(rule.getConditions().getExpressions().isEmpty()) && (rule.getActions() != null)
					&& (rule.getActions().getExpressions() != null) && !(rule.getActions().getExpressions().isEmpty())) {
				ExpressionValidator.validateRule(rule);

			} else if ((rule.getActions() == null) || (rule.getActions().getExpressions() == null)
					|| (rule.getActions().getExpressions().isEmpty())) {
				// Some rules don't have actions defined by the user (like the
				// group rules)
				ExpressionValidator.validateConditions(rule.getConditions());
			} else {
				ExpressionValidator.validateActions(rule.getActions());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (ruleName != null) {
				throw new InvalidRuleException("TableRule invalid", ruleName);
			} else {
				throw new InvalidRuleException("Rule invalid", rule.getName());
			}
		}
	}

	/**
	 * Checks if the rule is valid<br>
	 * Invalid rules throw an exception
	 * 
	 * @throws InvalidRuleException
	 */

	public static void checkRule(Rule rule) throws InvalidRuleException {
		checkRule(rule, null);
	}
}
