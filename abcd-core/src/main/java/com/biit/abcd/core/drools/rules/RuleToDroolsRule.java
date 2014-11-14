package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementOrVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * Transforms a Rule to a Drools rule. Internally is the same. This class is
 * used for standardization purposes.
 * 
 */
public class RuleToDroolsRule {

	private static List<DroolsRule> droolsRules;

	public static List<DroolsRule> parse(Rule rule, ExpressionChain extraConditions) throws RuleInvalidException,
			RuleNotImplementedException {
		DroolsRule droolsRule = null;
		if (rule != null) {
			if (!hasOrConditions(rule, extraConditions)) {
				droolsRule = new DroolsRule(rule.generateCopy());
				// TODO Uncomment when changed the validator to the parser
				// RuleChecker.checkRuleValid(droolsRule);
				droolsRule.setName(RulesUtils.getRuleName(droolsRule.getName(), extraConditions));
				if (extraConditions != null) {
					droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
				}
				droolsRules = Arrays.asList(droolsRule);
			}
		}
		return droolsRules;
	}

	private static boolean hasOrConditions(Rule rule, ExpressionChain extraConditions) {
		Rule copyRule = rule.generateCopy();
		ITreeElement result = calculatePrattParserResult(copyRule.getConditions());
		TreeElementOrVisitor treePrint = new TreeElementOrVisitor();
		try {
			result.accept(treePrint);
			if (!treePrint.getConditions().isEmpty()) {
				int ruleCounter = 1;
				droolsRules = new ArrayList<DroolsRule>();
				for (ExpressionChain visitorRules : treePrint.getConditions()) {
					DroolsRuleGroup droolsRule = new DroolsRuleGroup();
					droolsRule.setConditions(visitorRules);
					droolsRule.setActions(copyRule.getActions());
					droolsRule.setName(RulesUtils.getRuleName(copyRule.getName() + "_" + ruleCounter, extraConditions));
					if (extraConditions != null) {
						droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
					}
					// Set the special parameters for the group rules
					droolsRule.setGroupCondition("\t$groupRuleId : GroupRuleFired(!isRuleFired('" + copyRule.getName()
							+ "'))\n");
					droolsRule.setGroupAction("\t$groupRuleId.addRuleFired(\"" + copyRule.getName() + "\");\n"
							+ "\tupdate($groupRuleId);\n");
					droolsRules.add(droolsRule);
					ruleCounter++;
				}
				return true;
			}
		} catch (NotCompatibleTypeException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		try {
			prattParserResult = prattParser.parseExpression();
		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(DroolsParser.class.getName(), ex);
		}
		return prattParserResult;
	}
}
