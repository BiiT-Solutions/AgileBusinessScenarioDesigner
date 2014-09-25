package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
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
			DiagramCalculation expressionNode = (DiagramCalculation) node;
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
			if (node.getType().equals(DiagramObjectType.FORK)) {
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
		ExpressionValueTreeObjectReference treeObjectOfForkNode = forkNode.getReference();
		// For each outgoing link a new condition is created
		for (DiagramLink outLink : forkNode.getOutgoingLinks()) {
			ExpressionChain expressionOfLinkCopy = outLink.getExpressionChain().generateCopy();

			System.out.println("EXPRESSION LINK OF THE FORK: " + expressionOfLinkCopy);

			// Add the previous conditions in case there are nested forks
			if ((previousConditions != null) && (previousConditions.getExpressions() != null)
					&& !(previousConditions.getExpressions().isEmpty())) {
				expressionOfLinkCopy.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
				expressionOfLinkCopy.addExpressions(previousConditions.getExpressions());
			}

			TreeObject treeObject = treeObjectOfForkNode.getReference();
			if ((treeObject instanceof Question)) {
				Question questionObject = (Question) treeObject;
				switch (questionObject.getAnswerType()) {
				case RADIO:
					// We have 'sex male' as expression in diagram links. We
					// need to add an equals to create a
					// complete expression 'sex=male'.
					expressionOfLinkCopy.getExpressions().add(1, new ExpressionOperatorLogic(AvailableOperator.EQUALS));

					break;
				case MULTI_CHECKBOX:
					// Fork with multicheckbox can cause problems. User can
					// select answers from both different
					// flows.
					break;
				case INPUT:
					// In case of input we only have to add a copy of the link,
					// which
					// we do at the bottom of the loop.
					break;
				}
			}
			// Add the condition of the fork path to the array of conditions
			forkConditions.add(expressionOfLinkCopy);
		}

		for (ExpressionChain forkExpressionChain : forkConditions) {
			// Only one condition implies empty link "others"
			// We have to negate the other conditions of the fork
			if (forkExpressionChain.getExpressions().size() == 1) {
				ExpressionChain resultOfNegation = new ExpressionChain();
				for (ExpressionChain forkExpressionChainToNegate : forkConditions) {
					if (!forkExpressionChainToNegate.equals(forkExpressionChain)) {
						resultOfNegation.addExpression(new ExpressionFunction(AvailableFunction.NOT));
						resultOfNegation.addExpression(forkExpressionChainToNegate);
						resultOfNegation.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
						resultOfNegation.addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
					}
				}
				// Remove the last AND
				resultOfNegation.getExpressions().remove(resultOfNegation.getExpressions().size() - 1);
				System.out.println("RESULT OF NEGATION: " + resultOfNegation);
			}
		}

		return forkConditions;
	}

}
