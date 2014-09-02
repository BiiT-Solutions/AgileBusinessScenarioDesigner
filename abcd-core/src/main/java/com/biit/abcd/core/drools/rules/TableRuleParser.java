package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
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

	public String parse(TableRule tableRule, String extraConditions) throws ExpressionInvalidException {
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
				newRules += this.createDroolsRule(this.preParseConditions(row.getConditions()), row.getAction()
						.getExpressions(), extraConditions);
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
	private List<Expression> preParseConditions(List<Expression> conditions) {
		List<Expression> preParsedConditions = new ArrayList<Expression>();

		// For each pair of conditions adds an AND, and between each pair adds
		// an EQUALS
		for (int index = 0; index < conditions.size(); index++) {
			Expression condition = conditions.get(index);
			if (((index != 0) && ((index % 2) == 0))) {
				preParsedConditions.add(new ExpressionOperatorLogic(AvailableOperator.AND));
			} else if ((index % 2) != 0) {
				if (((ExpressionChain) condition).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
					preParsedConditions.add(new ExpressionOperatorLogic(AvailableOperator.EQUALS));
				}
			}
			preParsedConditions.add(condition);
		}
		// System.out.println("PRE PARSED CONDITIONS: " + preParsedConditions);
		return preParsedConditions;
	}
}
