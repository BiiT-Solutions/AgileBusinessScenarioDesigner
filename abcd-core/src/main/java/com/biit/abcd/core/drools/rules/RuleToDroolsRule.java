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
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
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
					DroolsRule droolsRule = new DroolsRule();
					droolsRule.setConditions(visitorRules);
					droolsRule.setActions(copyRule.getActions());
					droolsRule.setName(RulesUtils.getRuleName(copyRule.getName() + "_" + ruleCounter, extraConditions));
					if (extraConditions != null) {
						droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
					}
					droolsRules.add(droolsRule);
					ruleCounter++;
				}
				return true;
			}
		} catch (NotCompatibleTypeException e) {
			e.printStackTrace();
		}
		return false;

		// for (Expression expression : rule.getConditions().getExpressions()) {
		// if (expression instanceof ExpressionOperatorLogic) {
		// if (((ExpressionOperatorLogic)
		// expression).getValue().equals(AvailableOperator.OR)) {
		// return true;
		// }
		// }
		// }
		// return false;
	}

	private static void generateMultipleRules(Rule rule, ExpressionChain extraConditions) {
		droolsRules = new ArrayList<DroolsRule>();
		Rule copiedRule = rule.generateCopy();
		int ruleCounter = 1;
		int lastOrPosition = 0;
		for (int i = 1; i < rule.getConditions().getExpressions().size() - 1; i++) {
			Expression expression = rule.getConditions().getExpressions().get(i);
			if (expression instanceof ExpressionOperatorLogic) {
				if (((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
					DroolsRule droolsRule = new DroolsRule();
					ExpressionChain conditions = new ExpressionChain();
					for (int j = lastOrPosition; j < i; j++) {
						conditions.addExpression(copiedRule.getConditions().getExpressions().get(j));
					}
					droolsRule.setConditions(conditions);
					droolsRule.setActions(copiedRule.getActions());
					droolsRule
							.setName(RulesUtils.getRuleName(copiedRule.getName() + "_" + ruleCounter, extraConditions));
					if (extraConditions != null) {
						droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
					}
					droolsRules.add(droolsRule);
					ruleCounter++;
					lastOrPosition = i + 1;
				}
			}
		}
		DroolsRule droolsRule = new DroolsRule();
		ExpressionChain conditions = new ExpressionChain();
		for (int j = lastOrPosition; j < rule.getConditions().getExpressions().size(); j++) {
			conditions.addExpression(copiedRule.getConditions().getExpressions().get(j));
		}
		droolsRule.setConditions(conditions);
		droolsRule.setActions(copiedRule.getActions());
		droolsRule.setName(RulesUtils.getRuleName(copiedRule.getName() + "_" + ruleCounter, extraConditions));
		if (extraConditions != null) {
			droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
		}
		droolsRules.add(droolsRule);
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
