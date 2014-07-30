package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.facts.inputform.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;

public class FormParser {

	private Form form;
	private String rules;

	public FormParser(Form form) throws ExpressionInvalidException{
		this.form = form;
		initParser();
	}

	private void initParser() throws ExpressionInvalidException{
		rules = "package com.biit.drools \n" +
				"import com.biit.abcd.core.drools.facts.inputform.* \n" +
				"import java.util.List \n" +
				"global java.io.PrintStream out \n";

		if(form!= null){
			if(form.getTableRules() != null){
				// For each table rule, transform the code to drools rules
				for(TableRule tableRule: form.getTableRules()){
					rules += TableRuleParser.parseTableRule(tableRule);
				}
			}
		}
	}

	public String getRules(){
		return rules;
	}

}
