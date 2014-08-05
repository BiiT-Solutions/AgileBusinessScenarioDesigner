package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class TableRuleParser {

	public static String parse(TableRule tableRule) throws ExpressionInvalidException {
		String newRules = "";
		String tableRuleName = tableRule.getName();
		int i = 0;

		// One rule for each row
		for (TableRuleRow row : tableRule.getRules()) {
			//			RuleChecker.checkRowExpressionValid(row, i + 1);
			newRules += Utils.getStartRuleString(tableRuleName + "_row_" + i);
			newRules += Utils.getAttributes();
			newRules += Utils.getWhenRuleString();
			newRules += createRule(row);
			newRules += Utils.getEndRuleString();
			i++;
		}
		return newRules;
	}



	private static String createRule(TableRuleRow row) {
		return RowRuleParser.parse(row.getConditions(), row.getAction());
	}
}
