package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

public class DiagramParser extends GenericParser {

	private String newRule = "";
	private List<String> forkConditions;

	public String parse(Diagram diagram) throws ExpressionInvalidException, RuleInvalidException {
		List<DiagramObject> diagramObjects = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjects) {
			// Start the algorithm for each diagram source defined in the main
			// diagram
			if (diagramObject instanceof DiagramSource) {
				this.parseDiagramElement((DiagramElement) diagramObject, "");
			}
		}
		return this.newRule;
	}

	/**
	 * Parses the nodes of the diagram using a depth search algorithm
	 *
	 * @param node
	 *            the node being parsed
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 */
	private void parseDiagramElement(DiagramElement node, String extraConditions) throws ExpressionInvalidException,
			RuleInvalidException {
		// Parse the corresponding node
		switch (node.getType()) {
		case TABLE:
			DiagramTable tableNode = (DiagramTable) node;
			if (tableNode.getTable() != null) {
				TableRuleParser tableRuleParser = new TableRuleParser();
				this.newRule += tableRuleParser.parse(tableNode.getTable(), extraConditions);
			}
			break;
		case RULE:
			DiagramRule ruleNode = (DiagramRule) node;
			if (ruleNode.getRule() != null) {
				RuleParser ruleParser = new RuleParser();
				this.newRule += ruleParser.parse(ruleNode.getRule(), extraConditions);
			}
			break;
		case CALCULATION:
			DiagramCalculation expressionNode = (DiagramCalculation) node;
			if (expressionNode.getFormExpression() != null) {
				ExpressionParser expParser = new ExpressionParser();
				this.newRule += expParser.parse(expressionNode.getFormExpression(), extraConditions);
			}
			break;
		case DIAGRAM_CHILD:
			DiagramChild diagramNode = (DiagramChild) node;
			if (diagramNode.getChildDiagram() != null) {
				this.parse(diagramNode.getChildDiagram());
			}
			break;
		case FORK:
			this.forkConditions = this.parseFork((DiagramFork) node, extraConditions);
			// for(String forkCond: this.forkConditions) {
			// System.out.println("FORK CONDITION: " + forkCond);
			// }
			break;
		case SINK:
			DiagramSink sinkExpressionNode = (DiagramSink) node;
			if (sinkExpressionNode.getFormExpression() != null) {
				ExpressionParser expParser = new ExpressionParser();
				this.newRule += expParser.parse(sinkExpressionNode.getFormExpression(), extraConditions);
			}
			break;
		default:
			break;
		}
		int linkNumber = 0;
		for (DiagramLink outLink : node.getOutgoingLinks()) {
			if (node.getType().equals(DiagramObjectType.FORK)) {
				this.parseDiagramElement(outLink.getTargetElement(), this.forkConditions.get(linkNumber));
			} else {
				this.parseDiagramElement(outLink.getTargetElement(), null);
			}
			linkNumber++;
		}
	}

	/**
	 * A fork adds some extra condition or conditions to the rules that happen
	 * after <br>
	 * A fork and its outgoing links define a condition that a question or a
	 * score must fulfill
	 *
	 * @return
	 */
	private List<String> parseFork(DiagramFork forkNode, String extraConditions) {
		List<String> forkConditions = new ArrayList<String>();
		// Get the element to be checked
		ExpressionValueTreeObjectReference expVal = forkNode.getReference();
		// The variable is checked against a score
		if (expVal instanceof ExpressionValueCustomVariable) {
			// TODO
		} else {
			// For each outgoing link a new condition is created
			for (DiagramLink outLink : forkNode.getOutgoingLinks()) {

				String childrenCondition = this.createDroolsRule(outLink.getExpressionChain().getExpressions(), null,
						extraConditions);
				// Add the condition of the fork path to the array of conditions
				forkConditions.add(childrenCondition);
			}
		}
		return forkConditions;
	}

}
