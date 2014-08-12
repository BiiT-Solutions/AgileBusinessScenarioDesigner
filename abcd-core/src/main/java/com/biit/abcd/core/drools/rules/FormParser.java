package com.biit.abcd.core.drools.rules;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biit.abcd.core.drools.DroolsGlobalVariable;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

public class FormParser {

	private Form form;
	private String rules;
	private List<GlobalVariable> globalVariables;
	private List<DroolsGlobalVariable> droolsGlobalVariables;

	public FormParser(Form form) throws ExpressionInvalidException, RuleInvalidException {
		this.form = form;
		this.initParser();
	}

	public FormParser(Form form, List<GlobalVariable> globalVariables) throws ExpressionInvalidException,
			RuleInvalidException {
		this.form = form;
		this.globalVariables = globalVariables;
		this.droolsGlobalVariables = new ArrayList<DroolsGlobalVariable>();

		this.initParser();
	}

	private void initParser() throws ExpressionInvalidException, RuleInvalidException {
		if (this.form != null) {
			this.rules = "package com.biit.drools \n";
			this.rules += "import com.biit.abcd.core.drools.facts.inputform.* \n";
			this.rules += "import com.biit.abcd.core.drools.utils.* \n";
			this.rules += "import java.util.Date \n";
			this.rules += "import java.util.List \n";
			// Creation of the global variables
			if((this.globalVariables != null) && !this.globalVariables.isEmpty()) {
				this.rules += this.parseGlobalVariables();
			}

			// Follow the diagram to parse and launch the rules
			List<Diagram> diagrams = this.form.getDiagrams();
			if (diagrams != null) {
				// Look for the root diagrams
				List<Diagram> rootDiagrams = new ArrayList<Diagram>();
				for (Diagram diagram : diagrams) {
					if (this.form.getDiagramParent(diagram) == null) {
						rootDiagrams.add(diagram);
					}
				}
				DiagramParser diagParser = new DiagramParser();
				// Parse the root diagrams
				for (Diagram diagram : rootDiagrams) {
					this.rules += diagParser.parse(diagram);
				}
			}

			// // First parse the table rule
			// if(this.form.getTableRules() != null){
			// // For each table rule, transform the code to drools rules
			// for(TableRule tableRule: this.form.getTableRules()){
			// this.rules += TableRuleParser.parse(tableRule);
			// }
			// }
			// // Second parse the standalone expressions
			// if(!this.form.getExpressionChain().isEmpty()){
			// for(ExpressionChain expression: this.form.getExpressionChain()){
			// this.rules += ExpressionParser.parse(expression);
			// }
			// }
			// // Third parse the rules defined in the rule editor
			// if(!this.form.getRules().isEmpty()){
			// RuleParser ruleParser = new RuleParser();
			// for(Rule rule: this.form.getRules()){
			// this.rules += ruleParser.parse(rule);
			// }
			// }
		}
	}

	/**
	 * Creates the global constants for the drools session
	 * Also stores in memory the value to be inserted before the facts
	 *
	 * @return The global constants in drools
	 */
	private String parseGlobalVariables() {
		String globalConstants = "";
		// In the GUI are called global variables, but regarding the forms are
		// constants
		if ((this.globalVariables != null) && !this.globalVariables.isEmpty()) {
			for (GlobalVariable globalVariable : this.globalVariables) {
				// First check if the data inside the variable has a valid date
				List<VariableData> varDataList = globalVariable.getData();
				if((varDataList != null) && !varDataList.isEmpty()){
					for(VariableData variableData : varDataList){

						Timestamp currentTime = new Timestamp(new Date().getTime());
						Timestamp initTime = variableData.getValidFrom();
						Timestamp endTime = variableData.getValidTo();
						// Sometimes endtime can be null, meaning that the variable data has no ending time
						if((currentTime.after(initTime) &&(endTime == null))
								|| (currentTime.after(initTime) && currentTime.before(endTime))){
							globalConstants += this.globalVariableString(globalVariable);
							this.droolsGlobalVariables.add(new DroolsGlobalVariable(globalVariable.getName(),
									variableData.getValue()));

//							this.globalVariableValues.put(globalVariable, variableData.getValue());
							break;
						}
					}
				}
			}
		}
		return globalConstants;
	}

	private String globalVariableString(GlobalVariable globalVariable){
		switch (globalVariable.getFormat()){
		case DATE:
			return "global java.util.Date " + globalVariable.getName()+"\n";
		case TEXT:
			return "global java.lang.String " + globalVariable.getName()+"\n";
		case POSTAL_CODE:
			return "global java.lang.String " + globalVariable.getName()+"\n";
		case NUMBER:
			return "global java.lang.Number " + globalVariable.getName()+"\n";
		default:
			return "";
		}
	}

	public List<DroolsGlobalVariable> getGlobalVariables(){
		return this.droolsGlobalVariables;
	}

	public String getRules() {
		return this.rules;
	}

}
