package com.biit.abcd.core.drools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;
import com.biit.abcd.core.drools.rules.FormParser;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;

public class Form2DroolsNoDrl {

	private KieManager km;

	/**
	 * Parses the vaadin form and loads the rules generated in the drools engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly defined. <br>
	 * This method doesn't create any global variables
	 * @param form form to be parsed
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 * @throws IOException
	 */
	public void parse(Form form) throws ExpressionInvalidException, RuleInvalidException, IOException {
		if(!form.getChildren().isEmpty()) {
			this.km = new KieManager();
			FormParser formRules;
			try {
				// Creation of the rules
				formRules = new FormParser(form);
//				System.out.println(formRules.getRules());
				Files.write(Paths.get("./src/test/resources/generatedRules.drl"), formRules.getRules().getBytes());
				// Load the rules in memory
				this.km.buildSessionRules(formRules.getRules());

			} catch (ExpressionInvalidException e) {
				throw e;
			}
		}
	}

	/**
	 * Parses the vaadin form and loads the rules generated in the drools engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly defined. <br>
	 * This method creates the global constants defined in the globalVariables array
	 * @param form form to be parsed
	 * @param globalVariables array  with the global constants to be created
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 * @throws IOException
	 */
	public void parse(Form form, List<GlobalVariable> globalVariables) throws ExpressionInvalidException, RuleInvalidException, IOException {
		if(!form.getChildren().isEmpty()) {
			this.km = new KieManager();
			FormParser formRules;
			try {
				// Creation of the rules
				formRules = new FormParser(form, globalVariables);
				System.out.println(formRules.getRules());
//				Files.write(Paths.get("./src/test/resources/generatedRules.drl"), formRules.getRules().getBytes());
				// Load the rules in memory
				this.km.buildSessionRules(formRules.getRules());
				// Creation of the global constants
				this.km.setGlobalVariables(formRules.getGlobalVariables());

			} catch (ExpressionInvalidException e) {
				throw e;
			}
		}
	}

	/**
	 * Loads the (Submitted)form as facts of the knowledge base of the drools engine. <br>
	 * It also starts the engine execution by firing all the rules inside the engine.
	 * @param form
	 */
	public void go(ISubmittedForm form){
		this.km.setFacts(Arrays.asList(form));
		this.km.execute();
	}
}
