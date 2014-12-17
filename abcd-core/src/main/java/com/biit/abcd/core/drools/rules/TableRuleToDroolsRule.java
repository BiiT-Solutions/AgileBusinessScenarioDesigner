package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.validators.RuleChecker;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

/**
 * Transforms a Table rule to a list of drools rules.
 * 
 */
public class TableRuleToDroolsRule {

	/**
	 * Convert a table rule in a list of rules. One rule for each row.
	 * 
	 * @param tableRule
	 * @param extraConditions
	 * @return
	 * @throws ExpressionInvalidException
	 * @throws RuleNotImplementedException
	 * @throws ActionNotImplementedException
	 * @throws InvalidRuleException
	 */
	public static List<DroolsRule> parse(TableRule tableRule, ExpressionChain extraConditions)
			throws ExpressionInvalidException, RuleNotImplementedException, ActionNotImplementedException,
			InvalidRuleException {
		List<DroolsRule> newRules = new ArrayList<>();
		if (tableRule != null) {
			String tableRuleName = tableRule.getName();
			int i = 0;
			for (TableRuleRow row : tableRule.getRules()) {
				if (row.getAction() != null && row.getAction().getExpressions() != null
						&& !row.getAction().getExpressions().isEmpty()) {
					DroolsRule newRule = new DroolsRule();
					ExpressionChain rowConditionExpression = convertTableRowToExpressionChain(row.getConditions());

					newRule.setName(RulesUtils.getRuleName(tableRuleName + "_row_" + i, extraConditions));

					newRule.setConditions(RulesUtils.flattenExpressionChain(rowConditionExpression));
					newRule.addExtraConditions(extraConditions);
					newRule.setActions(row.getAction());
					newRules.add(newRule);
				}
				i++;
			}
		}
		for (DroolsRule droolsRule : newRules) {
			RuleChecker.checkRule(droolsRule);
		}
		return newRules;
	}

	/**
	 * Adds the logic expressions that the table don't add
	 * 
	 * @param conditions
	 * @return
	 */
	private static ExpressionChain convertTableRowToExpressionChain(ExpressionChain conditions) {
		ExpressionChain preParsedConditions = new ExpressionChain();

		// For each pair of conditions adds an AND, and between each pair adds
		// an EQUALS
		for (int index = 0; index < conditions.getExpressions().size() - 1; index += 2) {
			Expression questionExpression = conditions.getExpressions().get(index);
			Expression answerExpression = conditions.getExpressions().get(index + 1);

			// Question not empty
			if ((questionExpression instanceof ExpressionValueTreeObjectReference)
					&& (((ExpressionValueTreeObjectReference) questionExpression).getReference() != null)
					&&
					// Answer not empty
					(answerExpression instanceof ExpressionChain)
					&& (((ExpressionChain) answerExpression).getExpressions() != null)
					&& (!((ExpressionChain) answerExpression).getExpressions().isEmpty())) {

				if (index > 0) {
					preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
				}
				preParsedConditions.addExpression(questionExpression);

				if (((ExpressionChain) answerExpression).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
					preParsedConditions.addExpression(new ExpressionOperatorLogic(AvailableOperator.EQUALS));
				}
				preParsedConditions.addExpression(answerExpression);
			}
		}
		return preParsedConditions;
	}
}
