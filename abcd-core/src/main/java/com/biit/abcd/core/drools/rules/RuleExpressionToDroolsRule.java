//package com.biit.abcd.core.drools.rules;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
//import com.biit.abcd.core.drools.prattparser.PrattParser;
//import com.biit.abcd.core.drools.prattparser.PrattParserException;
//import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
//import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupConditionFinderVisitor;
//import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupEndConditionFinderVisitor;
//import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
//import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
//import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
//import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
//import com.biit.abcd.core.drools.utils.RulesUtils;
//import com.biit.abcd.logger.AbcdLogger;
//import com.biit.abcd.persistence.entity.Category;
//import com.biit.abcd.persistence.entity.CustomVariable;
//import com.biit.abcd.persistence.entity.Form;
//import com.biit.abcd.persistence.entity.Group;
//import com.biit.abcd.persistence.entity.Question;
//import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
//import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
//import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
//import com.biit.abcd.persistence.entity.expressions.Expression;
//import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
//import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
//import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
//import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
//import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
//import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
//import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
//import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
//import com.biit.abcd.persistence.entity.expressions.Rule;
//import com.biit.form.TreeObject;
//
///**
// * Transforms a Rule to a Drools rule. Internally is the same. This class is
// * used for standardization purposes.
// * 
// */
//public class RuleExpressionToDroolsRule {
//
//	private static List<DroolsRule> droolsRules;
//
//	// public static List<DroolsRule> parse(DroolsRule droolsRule) throws
//	// RuleInvalidException,
//	// RuleNotImplementedException {
//	//
//	//
//	// }
//
//	public static List<DroolsRule> parse(Rule rule, ExpressionChain extraConditions) throws RuleInvalidException,
//			RuleNotImplementedException, ExpressionInvalidException {
//
//		droolsRules = new ArrayList<DroolsRule>();
//
//		List<DroolsRule> conditionsRules = RuleToDroolsRule.parse(rule, extraConditions);
//		for (DroolsRule droolsRule : conditionsRules) {
//			droolsRules.addAll(ExpressionToDroolsRule.parse(droolsRule));
//		}
//		return droolsRules;
//	}
//
//	/**
//	 * Return true if the conditions of the rule have AND/OR or NOT expressions
//	 * 
//	 * @param expressionChain
//	 * @return
//	 */
//	private static boolean hasAndOrNotConditions(ExpressionChain expressionChain) {
//		for (Expression expression : expressionChain.getExpressions()) {
//			if (expression instanceof ExpressionChain) {
//				hasAndOrNotConditions((ExpressionChain) expression);
//			} else {
//				if (expression instanceof ExpressionOperatorLogic) {
//					ExpressionOperatorLogic expressionOperatorLogic = (ExpressionOperatorLogic) expression;
//					if (expressionOperatorLogic.getValue().equals(AvailableOperator.AND)
//							|| expressionOperatorLogic.getValue().equals(AvailableOperator.OR)) {
//						return true;
//					}
//				} else if (expression instanceof ExpressionFunction) {
//					ExpressionFunction expressionFunction = (ExpressionFunction) expression;
//					if (expressionFunction.getValue().equals(AvailableFunction.NOT)) {
//						addBracketsToNotConditions(expressionChain, expression);
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * Add an extra parenthesis to NOT expressions
//	 * 
//	 * @param expressionChain
//	 * @return
//	 */
//	private static void addBracketsToNotConditions(ExpressionChain expressionChain, Expression notExpression) {
//		int notExpressionIndex = expressionChain.getExpressions().indexOf(notExpression);
//		Expression expression = expressionChain.getExpressions().get(notExpressionIndex + 1);
//
//		if (!(expression instanceof ExpressionSymbol)) {
//			expressionChain.getExpressions().add(notExpressionIndex + 1,
//					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
//		} else if (expression instanceof ExpressionSymbol
//				&& !(((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.LEFT_BRACKET))) {
//			expressionChain.getExpressions().add(notExpressionIndex + 1,
//					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
//		}
//	}
//
//	private static void parseAndOrNotConditions(Rule ruleCopy, ExpressionChain extraConditions) {
//		ITreeElement result = calculatePrattParserResult(ruleCopy.getConditions());
//		TreeElementGroupConditionFinderVisitor treeVisitor = new TreeElementGroupConditionFinderVisitor();
//		try {
//			result.accept(treeVisitor);
//			if (!treeVisitor.getConditions().isEmpty()) {
//				int ruleCounter = 1;
//				for (ExpressionChain visitorRules : treeVisitor.getConditions()) {
//					DroolsRuleGroup droolsRule = new DroolsRuleGroup();
//					droolsRule.setConditionExpressionChainId(visitorRules.getName());
//					droolsRule.setConditions(visitorRules);
//					if (ruleCopy instanceof DroolsRule) {
//						String ruleName = ruleCopy.getName().substring(0, ruleCopy.getName().length() - 2) + "_"
//								+ ruleCounter + "\"\n";
//						droolsRule.setName(ruleName);
//					} else {
//						String ruleName = ruleCopy.getName() + "_" + ruleCounter;
//						droolsRule.setName(RulesUtils.getRuleName(ruleName, extraConditions));
//					}
//					// Set the special actions for the group rules
//					String groupAction = "then\n";
//					groupAction += "\tAbcdLogger.debug(\"RuleFired\", \"Rule "
//							+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + " fired\");\n";
//					groupAction += "\tinsert ( new FiredRule(\""
//							+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n";
//					droolsRule.setGroupAction(groupAction);
//					droolsRules.add(droolsRule);
//					ruleCounter++;
//				}
//				createEndCombinationRule(result, ruleCopy, ruleCounter);
//			}
//		} catch (NotCompatibleTypeException e) {
//			AbcdLogger.errorMessage(RuleExpressionToDroolsRule.class.getName(), e);
//		}
//	}
//
//	private static void createEndCombinationRule(ITreeElement result, Rule ruleCopy, int ruleCounter) {
//		TreeElementGroupEndConditionFinderVisitor treeVisitor = new TreeElementGroupEndConditionFinderVisitor();
//		try {
//			result.accept(treeVisitor);
//			DroolsRuleGroupEndRule droolsRule = new DroolsRuleGroupEndRule();
//			droolsRule.setConditions(treeVisitor.getCompleteExpression());
//			droolsRule.setActions(ruleCopy.getActions());
//			if (ruleCopy instanceof DroolsRule) {
//				String ruleName = ruleCopy.getName().substring(0, ruleCopy.getName().length() - 2) + "_" + ruleCounter
//						+ "\"\n";
//				droolsRule.setName(ruleName);
//			} else {
//				droolsRule.setName(RulesUtils.getRuleName(ruleCopy.getName() + "_" + ruleCounter, null));
//			}
//			// Set the special conditions/actions for the group rules
//			droolsRule.setGroupCondition("\tnot( FiredRule( getRuleName() == '"
//					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "') ) and\n");
//			droolsRule.setGroupAction("\tinsert ( new FiredRule(\""
//					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n");
//			droolsRule.setParserResult(result);
//			for (DroolsRule generatedDroolsRule : droolsRules) {
//				droolsRule.putExpresionRuleIdentifiers(((DroolsRuleGroup) generatedDroolsRule)
//						.getConditionExpressionChainId(), generatedDroolsRule.getName().split(" ")[1].replace("\n", "")
//						.replace("\"", ""));
//			}
//			droolsRules.add(droolsRule);
//		} catch (NotCompatibleTypeException e) {
//			AbcdLogger.errorMessage(RuleExpressionToDroolsRule.class.getName(), e);
//		}
//	}
//
//	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) {
//		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
//		ITreeElement prattParserResult = null;
//		try {
//			prattParserResult = prattParser.parseExpression();
//		} catch (PrattParserException ex) {
//			AbcdLogger.errorMessage(DroolsParser.class.getName(), ex);
//		}
//		return prattParserResult;
//	}
//
//	private static List<DroolsRule> parseAction(ExpressionChain expressionChain, ExpressionChain extraConditions)
//			throws ExpressionInvalidException, RuleNotImplementedException, RuleInvalidException {
//		List<DroolsRule> droolsRules = null;
//
//		if (expressionChain != null) {
//			// If the expression is composed by a generic variable, we have to
//			// generate the set of rules that represents the generic
//			if (expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable) {
//				droolsRules = createExpressionRuleSet(expressionChain, extraConditions);
//
//			} else if ((expressionChain.getExpressions().get(0) instanceof ExpressionFunction)
//					&& ((ExpressionFunction) expressionChain.getExpressions().get(0)).getValue().equals(
//							AvailableFunction.IF)) {
//				droolsRules = createIfRuleSet(expressionChain, extraConditions);
//
//			} else {
//				droolsRules = Arrays.asList(createExpressionRule(expressionChain, extraConditions));
//			}
//		}
//		// for (DroolsRule droolsRule : droolsRules) {
//		// RuleChecker.checkRuleValid(droolsRule);
//		// }
//		return droolsRules;
//	}
//
//	/**
//	 * Transforms an expression into a rule
//	 * 
//	 * @param expressionChain
//	 * @param extraConditions
//	 * @return
//	 */
//	private static DroolsRule createExpressionRule(ExpressionChain expressionChain, ExpressionChain extraConditions) {
//		DroolsRule droolsRule = new DroolsRule();
//		droolsRule.setName(RulesUtils.getRuleName(expressionChain.getName(), extraConditions));
//		if (extraConditions != null) {
//			droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
//		}
//		// If the expression chain contains generic variables, we have to unwrap
//		// them
//
//		if (checkForGenericVariables(expressionChain)) {
//			ExpressionChain expressionChainUnwrapped = unwrapGenericVariables(expressionChain);
//			if (expressionChainUnwrapped != null) {
//				droolsRule.setActions(expressionChainUnwrapped);
//			} else {
//				// We don't want to create a rule if there is no actions
//				return null;
//			}
//		} else {
//			droolsRule.setActions((ExpressionChain) expressionChain.generateCopy());
//		}
//		return droolsRule;
//	}
//
//	private static List<DroolsRule> createIfRuleSet(ExpressionChain expressionChain, ExpressionChain extraConditions) {
//		List<DroolsRule> droolsRules = new ArrayList<DroolsRule>();
//		ExpressionChain ifCondition = new ExpressionChain();
//		ExpressionChain ifActionThen = new ExpressionChain();
//		ExpressionChain ifActionElse = new ExpressionChain();
//
//		int ifIndex = 0;
//		ExpressionChain expressionCopy = (ExpressionChain) expressionChain.generateCopy();
//		// Remove the IF from the expression
//		expressionCopy.removeFirstExpression();
//		// and the last parenthesis
//		expressionCopy.removeLastExpression();
//		for (Expression expression : expressionCopy.getExpressions()) {
//			if ((expression instanceof ExpressionSymbol)
//					&& ((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.COMMA)) {
//				ifIndex++;
//			} else {
//				if (ifIndex == 0) {
//					ifCondition.addExpression(expression);
//				} else if (ifIndex == 1) {
//					ifActionThen.addExpression(expression);
//				} else if (ifIndex == 2) {
//					ifActionElse.addExpression(expression);
//				}
//			}
//		}
//		// Creation of the rules that represent the if
//		DroolsRule ifThenRule = new DroolsRule();
//		ifThenRule.setName(RulesUtils.getRuleName(expressionChain.getName(), extraConditions));
//		ifThenRule.setConditions(ifCondition);
//		ifThenRule.setActions(ifActionThen);
//		droolsRules.add(ifThenRule);
//
//		DroolsRule ifElseRule = new DroolsRule();
//		ifElseRule.setName(RulesUtils.getRuleName(expressionChain.getName(), extraConditions));
//		ExpressionChain negatedCondition = new ExpressionChain();
//		// For the ELSE part we negate the Condition part
//		negatedCondition.addExpression(new ExpressionFunction(AvailableFunction.NOT));
//		negatedCondition.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
//		negatedCondition.addExpressions(ifCondition.getExpressions());
//		negatedCondition.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
//		ifElseRule.setConditions(negatedCondition);
//		ifElseRule.setActions(ifActionElse);
//		droolsRules.add(ifElseRule);
//
//		return droolsRules;
//	}
//
//	private static List<DroolsRule> createExpressionRuleSet(ExpressionChain expressionChain,
//			ExpressionChain extraConditions) {
//		List<DroolsRule> droolsRules = new ArrayList<DroolsRule>();
//		ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) expressionChain
//				.getExpressions().get(0);
//		List<TreeObject> treeObjects = new ArrayList<TreeObject>();
//		switch (expressionValueGenericCustomVariable.getType()) {
//		case CATEGORY:
//			treeObjects.addAll(expressionValueGenericCustomVariable.getVariable().getForm().getChildren());
//			break;
//		case GROUP:
//			for (TreeObject category : expressionValueGenericCustomVariable.getVariable().getForm().getChildren()) {
//				List<TreeObject> groups = category.getAll(Group.class);
//				// We need to reverse the groups to correctly generate the rules
//				// for nested groups
//				Collections.reverse(groups);
//				treeObjects.addAll(groups);
//			}
//			break;
//		case QUESTION_GROUP:
//		case QUESTION_CATEGORY:
//			// We only support Categories and groups on the left part of the
//			// generic assignation
//			break;
//		}
//		// For each category, we generate the expression to create a new rule
//		if (treeObjects != null && !treeObjects.isEmpty()) {
//			for (TreeObject category : treeObjects) {
//				ExpressionChain expressionChainCopy = (ExpressionChain) expressionChain.generateCopy();
//				ExpressionValueCustomVariable expValCat = new ExpressionValueCustomVariable(category,
//						expressionValueGenericCustomVariable.getVariable());
//				// Remove the generic
//				expressionChainCopy.removeExpression(0);
//				// Add the specific
//				expressionChainCopy.addExpression(0, expValCat);
//				// Add to the rule set
//				droolsRules.add(createExpressionRule(expressionChainCopy, extraConditions));
//			}
//		} else {
//			// We don't want to create a rule if there is no children
//			return null;
//		}
//		return droolsRules;
//	}
//
//	/**
//	 * Checks if there are generic variables at the right side of the
//	 * assignation expression
//	 * 
//	 * @return
//	 */
//	private static boolean checkForGenericVariables(ExpressionChain expressionChain) {
//		for (Expression expression : expressionChain.getExpressions()) {
//			if ((expression instanceof ExpressionValueGenericCustomVariable)
//					|| (expression instanceof ExpressionValueGenericVariable)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * We have to substitute the generic for the list of tree objects that
//	 * represent
//	 * 
//	 * @param expressionChain
//	 * @return
//	 */
//	private static ExpressionChain unwrapGenericVariables(ExpressionChain expressionChain) {
//
//		// // To avoid modifying the original expression
//		ExpressionChain expressionChainCopy = (ExpressionChain) expressionChain.generateCopy();
//		ExpressionValueCustomVariable expressionValueLeftTreeObject = (ExpressionValueCustomVariable) expressionChainCopy
//				.getExpressions().get(0);
//		// The rule is different if the variable to assign is a Form, a Category
//		// a Group or a Question
//		TreeObject leftTreeObject = expressionValueLeftTreeObject.getReference();
//		ExpressionChain generatedExpressionChain = new ExpressionChain();
//		for (int originalExpressionIndex = 0; originalExpressionIndex < expressionChainCopy.getExpressions().size(); originalExpressionIndex++) {
//			Expression expression = expressionChainCopy.getExpressions().get(originalExpressionIndex);
//			if (expression instanceof ExpressionValueGenericCustomVariable) {
//				// Unwrap the generic variables being analyzed
//				ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) expression;
//				CustomVariable customVariableOfGeneric = expressionValueGenericCustomVariable.getVariable();
//				List<TreeObject> treeObjects = getUnwrappedTreeObjects(expressionValueGenericCustomVariable,
//						leftTreeObject);
//
//				// We have to create a CustomVariable for each treeObject found
//				// and add it to the expression
//				if ((treeObjects != null) && (!treeObjects.isEmpty())) {
//					for (TreeObject treeObject : treeObjects) {
//						generatedExpressionChain.addExpression(new ExpressionValueCustomVariable(treeObject,
//								customVariableOfGeneric));
//						generatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
//					}
//				}
//			} else if (expression instanceof ExpressionValueGenericVariable) {
//				// Unwrap the generic variables being analyzed
//				List<TreeObject> treeObjects = getUnwrappedTreeObjects((ExpressionValueGenericVariable) expression,
//						leftTreeObject);
//				// We have to create a TreeObjecReference for each treeObject
//				// found
//				// and add it to the expression
//				if ((treeObjects != null) && (!treeObjects.isEmpty())) {
//					for (TreeObject treeObject : treeObjects) {
//						generatedExpressionChain.addExpression(new ExpressionValueTreeObjectReference(treeObject));
//						generatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
//					}
//				}
//			} else {
//				// if it is not a generic variable, we copy the same value
//				if (!((expression instanceof ExpressionSymbol) && (((ExpressionSymbol) expression).getValue()
//						.equals(AvailableSymbol.COMMA)))) {
//					generatedExpressionChain.addExpression(expression);
//				}
//			}
//		}
//
//		// Remove the last comma if there is one
//		// Condition to avoid errors in the parser
//		if (((generatedExpressionChain.getExpressions().get(generatedExpressionChain.getExpressions().size() - 2) instanceof ExpressionSymbol) && (((ExpressionSymbol) generatedExpressionChain
//				.getExpressions().get(generatedExpressionChain.getExpressions().size() - 2)).getValue()
//				.equals(AvailableSymbol.COMMA)))) {
//			generatedExpressionChain.removeExpression(generatedExpressionChain.getExpressions().size() - 2);
//		}
//		return generatedExpressionChain;
//	}
//
//	private static List<TreeObject> getUnwrappedTreeObjects(
//			ExpressionValueGenericVariable expressionValueGenericVariable, TreeObject leftTreeObject) {
//		List<TreeObject> treeObjects = null;
//		switch (expressionValueGenericVariable.getType()) {
//		case CATEGORY:
//			if (leftTreeObject instanceof Form) {
//				treeObjects = leftTreeObject.getAll(Category.class);
//			}
//			break;
//		case GROUP:
//			if ((leftTreeObject instanceof Category) || (leftTreeObject instanceof Group)) {
//				if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
//					treeObjects = new ArrayList<TreeObject>();
//					for (TreeObject child : leftTreeObject.getChildren()) {
//						if (child instanceof Group) {
//							treeObjects.add(child);
//						}
//					}
//				}
//			}
//			break;
//		case QUESTION_CATEGORY:
//			if (leftTreeObject instanceof Category) {
//				// We only want the questions for the category
//				if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
//					treeObjects = new ArrayList<TreeObject>();
//					for (TreeObject child : leftTreeObject.getChildren()) {
//						if (child instanceof Question) {
//							treeObjects.add(child);
//						}
//					}
//				}
//			}
//			break;
//		case QUESTION_GROUP:
//			if (leftTreeObject instanceof Group) {
//				// We only want the questions for the group
//				if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
//					treeObjects = new ArrayList<TreeObject>();
//					for (TreeObject child : leftTreeObject.getChildren()) {
//						if (child instanceof Question) {
//							treeObjects.add(child);
//						}
//					}
//				}
//			}
//			break;
//		}
//		return treeObjects;
//	}
//}
