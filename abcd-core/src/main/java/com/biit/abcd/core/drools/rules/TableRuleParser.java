package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
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
			RuleNotImplementedException {
		String newRules = "";
		if (tableRule != null) {
			String tableRuleName = tableRule.getName();
			int i = 0;
			// One rule for each row
			for (TableRuleRow row : tableRule.getRules()) {
				// RuleChecker.checkRowExpressionValid(row, i + 1);
				newRules += Utils.getStartRuleString(tableRuleName + "_row_" + i);
				newRules += Utils.getAttributes();
				newRules += Utils.getWhenRuleString();
				newRules += this.createDroolsRule(this.preParseConditions(row.getConditionChain()),
						row.getActionChain(), extraConditions);
				newRules += Utils.getEndRuleString();
				i++;
			}
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
		for (int index = 0; index < conditions.getExpressions().size(); index++) {
			Expression condition = conditions.getExpressions().get(index);
			if (((index != 0) && ((index % 2) == 0))) {
				preParsedConditions.getExpressions().add(new ExpressionOperatorLogic(AvailableOperator.AND));
			} else if ((index % 2) != 0) {
				if (((ExpressionChain) condition).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
					preParsedConditions.getExpressions().add(new ExpressionOperatorLogic(AvailableOperator.EQUALS));
				}
			}
			preParsedConditions.getExpressions().add(condition);
		}
		// System.out.println("PRE PARSED CONDITIONS: " + preParsedConditions);
		return preParsedConditions;
	}
}
