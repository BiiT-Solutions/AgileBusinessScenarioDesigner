package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;

public class FormToDroolsRulesParser {

	private Form form;
	private String rules;

	public FormToDroolsRulesParser(Form form){
		this.form = form;
		initParser();
	}

	private void initParser(){
		rules = "package com.biit.drools \n" +
				"import com.biit.drools.facts.inputform.* \n" +
				"import com.biit.orbeon.form.* \n" +
				"import java.util.List \n" +
				"global java.io.PrintStream out \n";

		// For each table rule, transform the code to drools rules
		if(form!= null){
			if(form.getTableRules() != null){
				for(TableRule tableRule: form.getTableRules()){
					rules += TableRuleParser.parseTableRule(form, tableRule);
				}
			}
		}
	}

	public String getRules(){
		return rules;
	}

}
