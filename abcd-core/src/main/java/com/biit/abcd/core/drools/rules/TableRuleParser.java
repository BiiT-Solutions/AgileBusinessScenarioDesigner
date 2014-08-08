package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
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
				newRules += this.createDroolsRule(row.getConditions(), row.getAction().getExpressions(),
						extraConditions);
				newRules += Utils.getEndRuleString();
				i++;
			}
		}
		return newRules;
	}
}
