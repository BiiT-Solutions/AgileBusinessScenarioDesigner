package com.biit.abcd.core.drools.rules.validators;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleChecker {

	public static void checkRule(Rule rule, String ruleName) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		if ((rule.getConditions() != null) && (rule.getConditions().getExpressions() != null) && !(rule.getConditions().getExpressions().isEmpty())
				&& (rule.getActions() != null) && (rule.getActions().getExpressions() != null) && !(rule.getActions().getExpressions().isEmpty())) {
			ExpressionValidator.validateRule(rule);

		} else if ((rule.getActions() == null) || (rule.getActions().getExpressions() == null) || (rule.getActions().getExpressions().isEmpty())) {
			// Some rules don't have actions defined by the user (like the
			// group rules)
			ExpressionValidator.validateConditions(rule.getConditions());
		} else {
			ExpressionValidator.validateActions(rule.getActions());
		}
	}

	/**
	 * Checks if the rule is valid<br>
	 * Invalid rules throw an exception
	 * 
	 * @throws InvalidRuleException
	 * @throws NotCompatibleTypeException
	 * @throws InvalidExpressionException
	 * @throws PrattParserException
	 */

	public static void checkRule(Rule rule) throws PrattParserException, InvalidExpressionException, NotCompatibleTypeException {
		checkRule(rule, null);
	}
}
