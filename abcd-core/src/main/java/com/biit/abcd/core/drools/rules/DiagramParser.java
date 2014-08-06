package com.biit.abcd.core.drools.rules;

import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;

public class DiagramParser {

	public String parse(Diagram diagram) throws ExpressionInvalidException, RuleInvalidException{
		String newRule = "";

		List<DiagramObject> diagramObjects = diagram.getDiagramObjects();
		for(DiagramObject diagramObject : diagramObjects){
			if(diagramObject instanceof DiagramElement){
				DiagramElement node = (DiagramElement)diagramObject;
				switch (node.getType()) {
				case TABLE:
					DiagramTable tableNode = (DiagramTable) node;
					if(tableNode.getTable() != null) {
						newRule += TableRuleParser.parse(tableNode.getTable());
					}
					break;
				case RULE:
					DiagramRule ruleNode = (DiagramRule) node;
					if(ruleNode.getRule() != null) {
						RuleParser ruleParser = new RuleParser();
						newRule += ruleParser.parse(ruleNode.getRule());
					}
					break;
				case CALCULATION:
					DiagramCalculation expressionNode = (DiagramCalculation) node;
					if(expressionNode.getFormExpression() != null) {
						newRule += ExpressionParser.parse(expressionNode.getFormExpression());
					}
					break;
				case DIAGRAM_CHILD:
					DiagramChild diagramNode = (DiagramChild) node;
					if(diagramNode.getChildDiagram() != null) {
						newRule += this.parse(diagramNode.getChildDiagram());
					}
					break;
				default:
					break;
				}

				System.out.println("Node type: " + node.getType().toString());
			}
		}

		return newRule;
	}

}
