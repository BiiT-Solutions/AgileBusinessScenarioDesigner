package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class TableRuleParser extends GenericParser {

	public TableRuleParser() {
		super();
	}

	public String parse(TableRule tableRule, String extraConditions) throws ExpressionInvalidException,
			RuleNotImplementedException, ActionNotImplementedException {
		String newRules = "";
		if (tableRule != null) {
			String tableRuleName = tableRule.getName();
			int i = 0;
			for (TableRuleRow row : tableRule.getRules()) {
				if (row.getActionChain() != null && row.getActionChain().getExpressions() != null
						&& !row.getActionChain().getExpressions().isEmpty()) {
					ExpressionChain auxConditions = this.preParseConditions(row.getConditionChain());
					// RuleChecker.checkExpressionValid(auxConditions);
					newRules += RulesUtils.getStartRuleString(tableRuleName + "_row_" + i);
					newRules += RulesUtils.getAttributes();
					newRules += RulesUtils.getWhenRuleString();
					newRules += this.createDroolsRule(auxConditions, row.getActionChain(), extraConditions);
					newRules += RulesUtils.getEndRuleString();
					// newRules = RulesUtils.addThenIfNeeded(newRules);
				}
				i++;
			}
			// checkActionComplete(tableRule);
		}
		return newRules;
	}

	/**
	 * Adds the logic expressions that the table don't add
	 * 
	 * @param conditions
	 * @return
	 */
	private ExpressionChain preParseConditions(ExpressionChain conditions) {
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

				if (((index != 0) && ((index % 2) == 0))) {
					preParsedConditions.getExpressions().add(new ExpressionOperatorLogic(AvailableOperator.AND));
				}
				preParsedConditions.getExpressions().add(questionExpression);

				if (((ExpressionChain) answerExpression).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
					preParsedConditions.getExpressions().add(new ExpressionOperatorLogic(AvailableOperator.EQUALS));
				}
				preParsedConditions.getExpressions().add(answerExpression);
			}
		}
//		System.out.println("PRE PARSED CONDITIONS: " + preParsedConditions);
		return preParsedConditions;
	}

	// private void checkActionComplete(TableRule tableRule) throws
	// ActionNotImplementedException{
	// int i = 0;
	// // One rule for each row
	// for (TableRuleRow row : tableRule.getRules()) {
	// if (row.getActionChain().getExpressions().isEmpty()) {
	// throw new ActionNotImplementedException("Action not implemented in row "
	// + (i + 1)
	// + " of tablerule", row.getConditionChain());
	// }
	// i++;
	// }
	// }
}
