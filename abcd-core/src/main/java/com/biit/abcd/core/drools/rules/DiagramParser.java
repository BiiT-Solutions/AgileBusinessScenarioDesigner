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
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class DiagramParser extends GenericParser {

	private String newRule = "";
	private List<String> forkConditions;

	public String parse(Diagram diagram) throws ExpressionInvalidException, RuleInvalidException,
			RuleNotImplementedException, ActionNotImplementedException {
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
	 * @throws RuleNotImplementedException
	 * @throws ActionNotImplementedException 
	 */
	private void parseDiagramElement(DiagramElement node, String extraConditions) throws ExpressionInvalidException,
			RuleInvalidException, RuleNotImplementedException, ActionNotImplementedException {
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
//				System.out.println("EXPRESSION EXTRA CONDITIONS: " + extraConditions);
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
//			for (String forkCond : this.forkConditions) {
//				System.out.println("FORK CONDITION: " + forkCond);
//			}
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
//				extraConditions +=  this.forkConditions.get(linkNumber);
				this.parseDiagramElement(outLink.getTargetElement(), this.forkConditions.get(linkNumber));
			} else {
//				System.out.println("EXTRA CONDITIONS: " + extraConditions);
				this.parseDiagramElement(outLink.getTargetElement(), extraConditions);
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
	 * @throws RuleNotImplementedException
	 */
	private List<String> parseFork(DiagramFork forkNode, String extraConditions) throws RuleNotImplementedException {
		List<String> forkConditions = new ArrayList<String>();
		// Get the element to be checked
		ExpressionValueTreeObjectReference expVal = forkNode.getReference();
		// The variable is checked against a score
		if (expVal instanceof ExpressionValueCustomVariable) {
			// TODO
		} else {
			// For each outgoing link a new condition is created
			for (DiagramLink outLink : forkNode.getOutgoingLinks()) {
				String childrenCondition = "";
				// List<Expression> conditions = Arrays.asList(expVal,
				// outLink.getExpressionChain());
				// Parse the conditions using the generic parser
				// String childrenCondition = this.createDroolsRule(conditions,
				// null, extraConditions);
				// System.out.println(outLink.getExpressionChain());
				// System.out.println(outLink.getExpressionChain().getExpressions().size());
				// for(Expression exp :
				// outLink.getExpressionChain().getExpressions()){
				// System.out.println("Expression class: " + exp.getClass());
				// }

				// System.out.println("INSIDE FORK, PARSING: " +
				// outLink.getExpressionChain().getRepresentation());

				TreeObject treeObject = expVal.getReference();
				if ((treeObject instanceof Question) && (((Question) treeObject).getAnswerType() != null)) {
					Question questionObject = (Question) treeObject;
					switch (questionObject.getAnswerType()) {
					case RADIO:
					case MULTI_CHECKBOX:
//						System.out.println(outLink.getExpressionChain().toString());
						List<Expression> expressionList = outLink.getExpressionChain().getExpressions();
						childrenCondition = this.createDroolsRule(new ExpressionChain(expressionList.get(0),
								new ExpressionOperatorLogic(AvailableOperator.EQUALS), expressionList.get(1)), null,
								extraConditions);
						break;
					default:
						//TODO
						break;
					}
				} else {
					childrenCondition = this.createDroolsRule(outLink.getExpressionChain(), null, extraConditions);
				}

//				System.out.println("CHILDREN CONDITION: " + childrenCondition);
				// Add the condition of the fork path to the array of conditions
				forkConditions.add(childrenCondition);
			}
		}
		return forkConditions;
	}

}
