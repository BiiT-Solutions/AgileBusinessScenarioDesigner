package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupEndConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.TreeObject;

/**
 * Transforms a Rule to a Drools rule. Internally is the same. This class is
 * used for standardization purposes.
 * 
 */
public class RuleToDroolsRule {

	private static List<DroolsRule> droolsRules;

	public static List<DroolsRule> parse(DroolsRule droolsRule) throws RuleInvalidException,
			RuleNotImplementedException, ExpressionInvalidException {

		if (droolsRule.getName().startsWith("rule \"")) {
			droolsRule.setName(droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", ""));
		}

		return parse(droolsRule, null);
	}

	public static List<DroolsRule> parse(Rule rule, ExpressionChain extraConditions) throws RuleInvalidException,
			RuleNotImplementedException, ExpressionInvalidException {
		List<DroolsRule> conditionsRules = new ArrayList<>();
		if (rule != null) {
			List<DroolsRule> droolsRuleList = null;
			Rule ruleCopy = rule.generateCopy();
			// Add extra conditions before parsing rule
			if (extraConditions != null) {
				ruleCopy.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
			}
			// Check for generics
			if (hasGenericVariables(ruleCopy.getConditions())) {
				droolsRuleList = unwrapGenericVariables(ruleCopy);
			}
			// Check for AND/OR/NOT expressions
			if (hasAndOrNotConditions(ruleCopy.getConditions())) {
				if (droolsRuleList != null) {
					droolsRuleList = parseAndOrNotConditions(droolsRuleList);
				} else {
					droolsRuleList = parseAndOrNotConditions(Arrays.asList(new DroolsRule(ruleCopy)));
				}
			}
			if (droolsRuleList != null) {
				// Set the name for the rules
				int ruleNumber = 0;
				for (DroolsRule droolsRule : droolsRuleList) {
					String ruleName = droolsRule.getName() + "_" + ruleNumber;
					droolsRule.setName(RulesUtils.getRuleName(ruleName, extraConditions));

					// Add identifiers to the drools rule end group
					if (droolsRule instanceof DroolsRuleGroupEndRule) {
						for (DroolsRule generatedDroolsRule : droolsRuleList) {
							if ((generatedDroolsRule instanceof DroolsRuleGroup)
									&& !(generatedDroolsRule instanceof DroolsRuleGroupEndRule)) {
								((DroolsRuleGroupEndRule) droolsRule)
										.putExpresionRuleIdentifiers(
												((DroolsRuleGroup) generatedDroolsRule).getConditionExpressionChainId(),
												generatedDroolsRule.getName().split(" ")[1].replace("\n", "").replace(
														"\"", ""));
							}
						}
					}
					conditionsRules.add(droolsRule);
					ruleNumber++;
				}
			} else {
				DroolsRule droolsRule = new DroolsRule(ruleCopy);
				// TODO Uncomment when changed the validator to the parser
				// RuleChecker.checkRuleValid(droolsRule);
				droolsRule.setName(RulesUtils.getRuleName(droolsRule.getName(), extraConditions));
				conditionsRules = Arrays.asList(droolsRule);
			}
		}

		droolsRules = new ArrayList<>();
		for (DroolsRule droolsRule : conditionsRules) {
			List<DroolsRule> actionsRules = ExpressionToDroolsRule.parse(droolsRule);
			if (actionsRules != null && !actionsRules.isEmpty()) {
				droolsRules.addAll(actionsRules);
			}
		}
		return droolsRules;
	}

	private static boolean hasGenericVariables(ExpressionChain conditions) {
		for (Expression expression : conditions.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				hasGenericVariables((ExpressionChain) expression);
			} else if ((expression instanceof ExpressionValueGenericVariable)
					|| (expression instanceof ExpressionValueGenericCustomVariable)) {
				return true;
			}
		}
		return false;
	}

	// Generate complete drools rules (conditions/actions) with the conditions
	// variables unwrapped
	private static List<DroolsRule> unwrapGenericVariables(Rule rule) {
		List<DroolsRule> unwrappedRules = new ArrayList<DroolsRule>();
		List<TreeObject> unwrappedObjects = null;

		for (int originalExpressionIndex = 0; originalExpressionIndex < rule.getConditions().getExpressions().size(); originalExpressionIndex++) {
			Expression expression = rule.getConditions().getExpressions().get(originalExpressionIndex);
			if (expression instanceof ExpressionValueGenericCustomVariable) {
				// Unwrap the generic variables being analyzed
				ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) expression;
				CustomVariable customVariableOfGeneric = expressionValueGenericCustomVariable.getVariable();
				unwrappedObjects = getUnwrappedTreeObjects(expressionValueGenericCustomVariable,
						customVariableOfGeneric.getForm());
				break;
			} else if (expression instanceof ExpressionValueGenericVariable) {
				unwrappedObjects = getUnwrappedTreeObjects((ExpressionValueGenericVariable) expression);
				break;
			}
		}
		if (unwrappedObjects != null && !unwrappedObjects.isEmpty()) {
			for (TreeObject treeObject : unwrappedObjects) {
				DroolsRule droolsRule = new DroolsRule();
				for (int originalExpressionIndex = 0; originalExpressionIndex < rule.getConditions().getExpressions()
						.size(); originalExpressionIndex++) {
					Expression expression = rule.getConditions().getExpressions().get(originalExpressionIndex);
					
					if (expression instanceof ExpressionValueGenericCustomVariable) {
						CustomVariable customVariableOfGeneric = ((ExpressionValueGenericCustomVariable) expression)
								.getVariable();
						droolsRule.getConditions().addExpression(
								new ExpressionValueCustomVariable(treeObject, customVariableOfGeneric));

					} else if (expression instanceof ExpressionValueGenericVariable) {
						droolsRule.getConditions().addExpression(new ExpressionValueTreeObjectReference(treeObject));

					} else {
						droolsRule.getConditions().addExpression(expression);
					}
				}
				unwrappedRules.add(droolsRule);
			}
		}

		// Copy the name and the actions of the rule
		for (DroolsRule droolsRule : unwrappedRules) {
			droolsRule.setName(rule.getName());
			droolsRule.setActions(rule.getActions());
		}
		return unwrappedRules;
	}

	private static List<TreeObject> getUnwrappedTreeObjects(
			ExpressionValueGenericVariable expressionValueGenericVariable, Form form) {
		List<TreeObject> treeObjects = null;
		switch (expressionValueGenericVariable.getType()) {
		case CATEGORY:
			treeObjects = form.getAll(Category.class);
			break;
		case GROUP:
			treeObjects = form.getAll(Group.class);
			break;
		case QUESTION_CATEGORY:
			List<TreeObject> categories = form.getAll(Category.class);
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject category : categories) {
				treeObjects.addAll(category.getChildren(Question.class));
			}
			break;
		case QUESTION_GROUP:
			List<TreeObject> groups = form.getAll(Group.class);
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject group : groups) {
				treeObjects.addAll(group.getChildren(Question.class));
			}
			break;
		}
		return treeObjects;
	}

	/**
	 * Get the brothers of the tree object passed
	 * 
	 * @param expressionValueGenericVariable
	 * @return
	 */
	private static List<TreeObject> getUnwrappedTreeObjects(
			ExpressionValueGenericVariable expressionValueGenericVariable) {
		List<TreeObject> treeObjects = null;
		Form form = expressionValueGenericVariable.getForm();
		switch (expressionValueGenericVariable.getType()) {
		case CATEGORY:
			treeObjects = form.getAll(Category.class);
			break;
		case GROUP:
			treeObjects = form.getAll(Group.class);
			break;
		case QUESTION_CATEGORY:
			List<TreeObject> categories = form.getAll(Category.class);
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject category : categories) {
				treeObjects.addAll(category.getChildren(Question.class));
			}
			break;
		case QUESTION_GROUP:
			List<TreeObject> groups = form.getAll(Group.class);
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject group : groups) {
				treeObjects.addAll(group.getChildren(Question.class));
			}
			break;
		}
		return treeObjects;
	}

	/**
	 * Return true if the conditions of the rule have AND/OR or NOT expressions
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static boolean hasAndOrNotConditions(ExpressionChain expressionChain) {
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				hasAndOrNotConditions((ExpressionChain) expression);
			} else {
				if (expression instanceof ExpressionOperatorLogic) {
					ExpressionOperatorLogic expressionOperatorLogic = (ExpressionOperatorLogic) expression;
					if (expressionOperatorLogic.getValue().equals(AvailableOperator.AND)
							|| expressionOperatorLogic.getValue().equals(AvailableOperator.OR)) {
						return true;
					}
				} else if (expression instanceof ExpressionFunction) {
					ExpressionFunction expressionFunction = (ExpressionFunction) expression;
					if (expressionFunction.getValue().equals(AvailableFunction.NOT)) {
						addBracketsToNotConditions(expressionChain, expression);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Add an extra parenthesis to NOT expressions
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static void addBracketsToNotConditions(ExpressionChain expressionChain, Expression notExpression) {
		int notExpressionIndex = expressionChain.getExpressions().indexOf(notExpression);
		Expression expression = expressionChain.getExpressions().get(notExpressionIndex + 1);

		if (!(expression instanceof ExpressionSymbol)) {
			expressionChain.getExpressions().add(notExpressionIndex + 1,
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		} else if (expression instanceof ExpressionSymbol
				&& !(((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.LEFT_BRACKET))) {
			expressionChain.getExpressions().add(notExpressionIndex + 1,
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		}
	}

	private static List<DroolsRule> parseAndOrNotConditions(List<DroolsRule> droolsRuleList) {
		List<DroolsRule> newConditions = new ArrayList<DroolsRule>();
		for (DroolsRule droolsRule : droolsRuleList) {
			ITreeElement result = calculatePrattParserResult(droolsRule.getConditions());
			TreeElementGroupConditionFinderVisitor treeVisitor = new TreeElementGroupConditionFinderVisitor();
			try {
				result.accept(treeVisitor);
				if (!treeVisitor.getConditions().isEmpty()) {
					for (ExpressionChain visitorRules : treeVisitor.getConditions()) {
						DroolsRuleGroup droolsRuleGroup = new DroolsRuleGroup();
						droolsRuleGroup.setName(droolsRule.getName());
						droolsRuleGroup.setConditionExpressionChainId(visitorRules.getName());
						droolsRuleGroup.setConditions(visitorRules);
						newConditions.add(droolsRuleGroup);
					}
					createEndCombinationRule(result, newConditions, droolsRule);
				}
			} catch (NotCompatibleTypeException e) {
				AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
			}
		}
		return newConditions;
	}

	private static DroolsRule createEndCombinationRule(ITreeElement result, List<DroolsRule> newConditions,
			DroolsRule droolsRule) {
		DroolsRuleGroupEndRule droolsEngGroupRule = null;
		TreeElementGroupEndConditionFinderVisitor treeVisitor = new TreeElementGroupEndConditionFinderVisitor();
		try {
			result.accept(treeVisitor);
			droolsEngGroupRule = new DroolsRuleGroupEndRule();
			droolsEngGroupRule.setName(droolsRule.getName());
			droolsEngGroupRule.setConditions(treeVisitor.getCompleteExpression());
			// Set the special conditions/actions for the group rules
			droolsEngGroupRule.setParserResult(result);
			droolsEngGroupRule.setActions(droolsRule.getActions());
			newConditions.add(droolsEngGroupRule);
		} catch (NotCompatibleTypeException e) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
		}
		return droolsEngGroupRule;
	}

	private static void parseAndOrNotConditions(Rule ruleCopy, ExpressionChain extraConditions) {
		ITreeElement result = calculatePrattParserResult(ruleCopy.getConditions());
		TreeElementGroupConditionFinderVisitor treeVisitor = new TreeElementGroupConditionFinderVisitor();
		try {
			result.accept(treeVisitor);
			if (!treeVisitor.getConditions().isEmpty()) {
				int ruleCounter = 1;
				for (ExpressionChain visitorRules : treeVisitor.getConditions()) {
					DroolsRuleGroup droolsRule = new DroolsRuleGroup();
					droolsRule.setConditionExpressionChainId(visitorRules.getName());
					droolsRule.setConditions(visitorRules);
					if (ruleCopy instanceof DroolsRule) {
						String ruleName = ruleCopy.getName().substring(0, ruleCopy.getName().length() - 2) + "_"
								+ ruleCounter + "\"\n";
						droolsRule.setName(ruleName);
					} else {
						String ruleName = ruleCopy.getName() + "_" + ruleCounter;
						droolsRule.setName(RulesUtils.getRuleName(ruleName, extraConditions));
					}
					// Set the special actions for the group rules
					String groupAction = "then\n";
					groupAction += "\tAbcdLogger.debug(\"RuleFired\", \"Rule "
							+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + " fired\");\n";
					groupAction += "\tinsert ( new FiredRule(\""
							+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n";
					droolsRule.setGroupAction(groupAction);
					droolsRules.add(droolsRule);
					ruleCounter++;
				}
				createEndCombinationRule(result, ruleCopy, ruleCounter);
			}
		} catch (NotCompatibleTypeException e) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
		}
	}

	private static void createEndCombinationRule(ITreeElement result, Rule ruleCopy, int ruleCounter) {
		TreeElementGroupEndConditionFinderVisitor treeVisitor = new TreeElementGroupEndConditionFinderVisitor();
		try {
			result.accept(treeVisitor);
			DroolsRuleGroupEndRule droolsRule = new DroolsRuleGroupEndRule();
			droolsRule.setConditions(treeVisitor.getCompleteExpression());
			droolsRule.setActions(ruleCopy.getActions());
			if (ruleCopy instanceof DroolsRule) {
				String ruleName = ruleCopy.getName().substring(0, ruleCopy.getName().length() - 2) + "_" + ruleCounter
						+ "\"\n";
				droolsRule.setName(ruleName);
			} else {
				droolsRule.setName(RulesUtils.getRuleName(ruleCopy.getName() + "_" + ruleCounter, null));
			}
			// Set the special conditions/actions for the group rules
			droolsRule.setGroupCondition("\tnot( FiredRule( getRuleName() == '"
					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "') ) and\n");
			droolsRule.setGroupAction("\tinsert ( new FiredRule(\""
					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n");
			droolsRule.setParserResult(result);
			for (DroolsRule generatedDroolsRule : droolsRules) {
				droolsRule.putExpresionRuleIdentifiers(((DroolsRuleGroup) generatedDroolsRule)
						.getConditionExpressionChainId(), generatedDroolsRule.getName().split(" ")[1].replace("\n", "")
						.replace("\"", ""));
			}
			droolsRules.add(droolsRule);
		} catch (NotCompatibleTypeException e) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
		}
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
