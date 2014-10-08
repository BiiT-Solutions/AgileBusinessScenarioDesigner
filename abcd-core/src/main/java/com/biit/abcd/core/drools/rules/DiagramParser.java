package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.TreeObject;

public class DiagramParser {

	public String getDroolsRulesAsText(Diagram diagram) throws ExpressionInvalidException, RuleInvalidException,
			RuleNotImplementedException, ActionNotImplementedException {
		List<Rule> newRules = parse(diagram, null);
		String rulesAsString = DroolsParser.createDroolsRule(newRules);
		return rulesAsString;
	}

	private List<Rule> parse(Diagram diagram, ExpressionChain extraConditions) throws ExpressionInvalidException,
			RuleInvalidException, RuleNotImplementedException, ActionNotImplementedException {
		List<Rule> newRules = new ArrayList<>();
		List<DiagramObject> diagramNodes = diagram.getDiagramObjects();
		for (DiagramObject diagramNode : diagramNodes) {
			// Start the algorithm for each diagram source defined in the main
			// diagram
			if (diagramNode instanceof DiagramSource) {
				parseDiagramElement((DiagramElement) diagramNode, extraConditions, newRules);
			}
		}
		return newRules;
	}

	/**
	 * Parses the nodes of the diagram using a depth search algorithm
	 * 
	 * @param node
	 *            the node being parsed
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 * @throws RuleNotImplementedException
	 * @throws ActionNotImplementedException
	 */
	private List<Rule> parseDiagramElement(DiagramElement node, ExpressionChain extraConditions, List<Rule> newRules)
			throws ExpressionInvalidException, RuleInvalidException, RuleNotImplementedException,
			ActionNotImplementedException {
		List<ExpressionChain> forkConditions = new ArrayList<>();
		// Parse the corresponding node
		switch (node.getType()) {
		case TABLE:
			DiagramTable tableNode = (DiagramTable) node;
			if (tableNode.getTable() != null) {
				newRules.addAll(TableRuleParser.parse(tableNode.getTable(), extraConditions));
			}
			break;
		case RULE:
			DiagramRule ruleNode = (DiagramRule) node;
			if (ruleNode.getRule() != null) {
				newRules.add(RuleParser.parse(ruleNode.getRule(), extraConditions));
			}
			break;
		case CALCULATION:
			DiagramExpression expressionNode = (DiagramExpression) node;
			if (expressionNode.getFormExpression() != null) {
				newRules.addAll(ExpressionParser.parse(expressionNode.getFormExpression(), extraConditions));
			}
			break;
		case DIAGRAM_CHILD:
			DiagramChild diagramNode = (DiagramChild) node;
			if (diagramNode.getChildDiagram() != null) {
				newRules.addAll(parse(diagramNode.getChildDiagram(), extraConditions));
			}
			break;
		case FORK:
			forkConditions = completeForkExpressions((DiagramFork) node, extraConditions);
			break;
		case SINK:
			DiagramSink sinkExpressionNode = (DiagramSink) node;
			if (sinkExpressionNode.getFormExpression() != null) {
				newRules.addAll(ExpressionParser.parse(sinkExpressionNode.getFormExpression(), extraConditions));
			}
			break;
		default:
			break;
		}
		int linkNumber = 0;
		for (DiagramLink outLink : node.getOutgoingLinks()) {
			if (node.getType().equals(DiagramObjectType.FORK) && (forkConditions.size() > linkNumber)) {
				parseDiagramElement(outLink.getTargetElement(), forkConditions.get(linkNumber), newRules);
			} else {
				parseDiagramElement(outLink.getTargetElement(), extraConditions, newRules);
			}
			linkNumber++;
		}
		return newRules;
	}

	/**
	 * A fork adds some extra condition or conditions to the rules that happen
	 * after <br>
	 * A fork and its outgoing links define a condition that a question or a
	 * score must fulfill
	 * 
	 * @return
	 * @throws RuleNotImplementedException
	 * @throws RuleInvalidException
	 */
	private List<ExpressionChain> completeForkExpressions(DiagramFork forkNode, ExpressionChain previousConditions)
			throws RuleNotImplementedException, RuleInvalidException {
		List<ExpressionChain> forkConditions = new ArrayList<>();

		// Get the element to be checked
		ExpressionValueTreeObjectReference forkNodeExpression = forkNode.getReference();
		// For each outgoing link a new condition is created
		for (DiagramLink outLink : forkNode.getOutgoingLinks()) {
			ExpressionChain expressionOfLinkCopy = outLink.getExpressionChain().generateCopy();

			// System.out.println("EXPRESSION LINK OF THE FORK: " +
			// expressionOfLinkCopy);

			if (forkNodeExpression != null) {
				TreeObject treeObject = forkNodeExpression.getReference();
				if ((treeObject instanceof Question)) {
					Question questionObject = (Question) treeObject;
					switch (questionObject.getAnswerType()) {
					case RADIO:
						// We have 'sex male' as expression in diagram links. We
						// need to add an equals to create a
						// complete expression 'sex == male'.
						if ((expressionOfLinkCopy.getExpressions().size() > 1)
								&& !(expressionOfLinkCopy.getExpressions().get(1) instanceof ExpressionOperatorLogic)) {
							expressionOfLinkCopy.getExpressions().add(1,
									new ExpressionOperatorLogic(AvailableOperator.EQUALS));
						} else {
							expressionOfLinkCopy.getExpressions().clear();
						}

						break;
					case MULTI_CHECKBOX:
						// Fork with multicheckbox can cause problems. User can
						// select answers from both different
						// flows.
						break;
					case INPUT:
						// In case of input we only have to add a copy of the
						// link, which we do at the bottom of the loop.
						if (expressionOfLinkCopy.getExpressions().size() <= 1) {
							expressionOfLinkCopy.getExpressions().clear();
						}
						break;
					}
				}
				// System.out.println("EXPRESSION ADDED TO FORK CONDITIONS: " +
				// expressionOfLinkCopy);
				// Add the condition of the fork path to the array of conditions
				forkConditions.add(expressionOfLinkCopy);
			}
		}

		// System.out.println("FORK CONDITIONS: " + forkConditions);

		int forkConditionToRemove = 0;
		for (ExpressionChain forkExpressionChain : forkConditions) {
			// Only one condition implies empty link "others"
			// We have to negate the other conditions of the fork
			if (forkExpressionChain.getExpressions().isEmpty()) {
				ExpressionChain resultOfNegation = new ExpressionChain();
				for (ExpressionChain forkExpressionChainToNegate : forkConditions) {
					if (!forkExpressionChainToNegate.equals(forkExpressionChain)) {

						if (chekForAndsOrOrs(forkExpressionChainToNegate)) {
							// System.out.println("CHAIN NEGATED: " +
							// negateAndOrConditions(forkExpressionChainToNegate));
							resultOfNegation.addExpressions(negateAndOrConditions(forkExpressionChainToNegate)
									.getExpressions());
							resultOfNegation.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
						} else {
							resultOfNegation.addExpression(new ExpressionFunction(AvailableFunction.NOT));
							resultOfNegation.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
							resultOfNegation.addExpression(forkExpressionChainToNegate.generateCopy());
							resultOfNegation.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
							resultOfNegation.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
						}
					}
				}
				// Remove the last AND
				if (resultOfNegation.getExpressions().size() > 1) {
					resultOfNegation.getExpressions().remove(resultOfNegation.getExpressions().size() - 1);
				}
				// System.out.println("RESULT OF NEGATION: " +
				// resultOfNegation);
				forkConditions.set(forkConditionToRemove, resultOfNegation);
			}
			forkConditionToRemove++;
		}

		// System.out.println("FORK CONDITIONS AFTER NEGATION, BEFORE PREVIOUS CONDITIONS: "
		// + forkConditions);

		if ((previousConditions != null) && (previousConditions.getExpressions() != null)
				&& !(previousConditions.getExpressions().isEmpty())) {
			for (ExpressionChain forkExpressionChain : forkConditions) {

				// System.out.println("FORK EXPRESSION CHAIN BEFORE: " +
				// forkExpressionChain);
				// System.out.println("PREVIOUS EXPRESSIONS: " +
				// previousConditions);

				forkExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
				forkExpressionChain.addExpressions(previousConditions.getExpressions());

				// System.out.println("FORK EXPRESSION CHAIN AFTER: " +
				// forkExpressionChain);
			}
		}

		// System.out.println("FORK CONDITIONS AFTER NEGATION, AFTER PREVIOUS CONDITIONS: "
		// + forkConditions);

		return forkConditions;
	}

	private static boolean chekForAndsOrOrs(ExpressionChain expressionChain) {
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionOperatorLogic) {
				if (((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.AND)
						|| ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
					return true;
				}
			}
		}
		return false;
	}

	private static List<ExpressionChain> divideConditions(ExpressionChain expressionChain) {
		List<ExpressionChain> expressionChainList = new ArrayList<ExpressionChain>();

		ExpressionChain newExpressionChain = new ExpressionChain();
		for (Expression expression : expressionChain.getExpressions()) {
			if ((expression instanceof ExpressionOperatorLogic)
					&& ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
				expressionChainList.add(newExpressionChain);
				expressionChainList.add(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.OR)));
				newExpressionChain = new ExpressionChain();
			} else {
				newExpressionChain.addExpression(expression);
			}
		}
		expressionChainList.add(newExpressionChain);
		return expressionChainList;
	}

	private static ExpressionChain negateAndOrConditions(ExpressionChain expressionChain) {
		ExpressionChain negatedExpressionChain = new ExpressionChain();
		negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
		negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		for (Expression expression : expressionChain.getExpressions()) {
			if ((expression instanceof ExpressionOperatorLogic)
					&& ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
				negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
				negatedExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
				negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
				negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
			} else if ((expression instanceof ExpressionOperatorLogic)
					&& ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.AND)) {
				negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
				negatedExpressionChain.addExpression(new ExpressionOperatorLogic(AvailableOperator.OR));
				negatedExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.NOT));
				negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
			} else {
				negatedExpressionChain.addExpression(expression);
			}
		}
		negatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		return negatedExpressionChain;
	}

}
