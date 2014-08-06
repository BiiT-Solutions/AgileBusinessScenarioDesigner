package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;

public class FormParser {

	private Form form;
	private String rules;

	public FormParser(Form form) throws ExpressionInvalidException, RuleInvalidException{
		this.form = form;
		this.initParser();
	}

	private void initParser() throws ExpressionInvalidException, RuleInvalidException{
		if(this.form!= null){
			this.rules = "package com.biit.drools \n" +
					"import com.biit.abcd.core.drools.facts.inputform.* \n" +
					"import java.util.List \n" +
					"global java.io.PrintStream out \n";

			// Follow the diagram to parse and launch the rules
			List<Diagram> diagrams = this.form.getDiagrams();

			if(diagrams != null){
				// Look for the root diagrams
				List<Diagram> rootDiagrams = new ArrayList<Diagram>();
				for(Diagram diagram: diagrams){
					if(this.form.getDiagramParent(diagram) == null){
						rootDiagrams.add(diagram);
					}
				}
				DiagramParser diagParser = new DiagramParser();
				// Parse the root diagrams
				for(Diagram diagram: rootDiagrams){
					this.rules += diagParser.parse(diagram);
				}
			}


//			// First parse the table rule
//			if(this.form.getTableRules() != null){
//				// For each table rule, transform the code to drools rules
//				for(TableRule tableRule: this.form.getTableRules()){
//					this.rules += TableRuleParser.parse(tableRule);
//				}
//			}
//			// Second parse the standalone expressions
//			if(!this.form.getExpressionChain().isEmpty()){
//				for(ExpressionChain expression: this.form.getExpressionChain()){
//					this.rules += ExpressionParser.parse(expression);
//				}
//			}
//			// Third parse the rules defined in the rule editor
//			if(!this.form.getRules().isEmpty()){
//				RuleParser ruleParser = new RuleParser();
//				for(Rule rule: this.form.getRules()){
//					this.rules += ruleParser.parse(rule);
//				}
//			}
		}
	}

	public String getRules(){
		return this.rules;
	}

}
