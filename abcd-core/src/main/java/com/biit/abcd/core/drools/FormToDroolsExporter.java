package com.biit.abcd.core.drools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleExecutionException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.form.DroolsForm;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.implementation.SubmittedForm;

public class FormToDroolsExporter {

	/**
	 * Parses the abcd form and loads the rules generated in the drools engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly defined. <br>
	 * This method creates the global constants defined in the globalVariables array
	 * 
	 * @param form
	 *            form to be parsed
	 * @param globalVariables
	 *            array with the global constants to be created
	 * @throws DroolsRuleGenerationException
	 */
	public DroolsRulesGenerator generateDroolRules(Form form, List<GlobalVariable> globalVariables)
			throws DroolsRuleGenerationException {
		if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
			DroolsRulesGenerator formRules;

			// Creation of the rules
			formRules = new DroolsRulesGenerator(form, globalVariables);
			AbcdLogger.debug(this.getClass().getName(), formRules.getRules());
			try {
				Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedRules.drl"),
						formRules.getRules().getBytes("UTF-8"));
			} catch (IOException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			return formRules;

		}
		return null;
	}

	public String getDroolRules(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException {
		if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
			DroolsRulesGenerator formRules;
			// Creation of the rules
			formRules = new DroolsRulesGenerator(form, globalVariables);
			return formRules.getRules();
		}
		return null;
	}

	/**
	 * Loads the (Submitted) form as facts of the knowledge base of the drools engine. <br>
	 * It also starts the engine execution by firing all the rules inside the engine.
	 * 
	 * @param form
	 */
	private void runDroolsRules(ISubmittedForm form, KieManager km) {
		if ((form != null) && (km != null)) {
			km.setFacts(Arrays.asList(form));
			km.execute();
		}
	}

	/**
	 * Process the test scenario. Orbeon not needed.
	 * 
	 * @param form
	 * @param globalVariables
	 * @return
	 * @throws DroolsRuleGenerationException
	 * @throws DroolsRuleExecutionException
	 */
	public ISubmittedForm processForm(Form form, List<GlobalVariable> globalVariables, ISubmittedForm iSubmittedForm)
			throws DroolsRuleGenerationException, DroolsRuleExecutionException {
		// Generate all drools rules.
		DroolsRulesGenerator rulesGenerator = generateDroolRules(form, globalVariables);
		// Obtain results
		if ((rulesGenerator != null) && (iSubmittedForm != null)) {
			return applyDrools(iSubmittedForm, rulesGenerator.getRules(), rulesGenerator.getGlobalVariables());
		} else
			return null;
	}

	/**
	 * Method used for the test scenarios
	 * 
	 * @param submittedForm
	 *            without scores
	 * @param droolsRules
	 * @param globalVariables
	 * @return submittedForm with the scores calculated by drools
	 * @throws DroolsRuleExecutionException
	 */
	public DroolsForm applyDrools(ISubmittedForm submittedForm, String droolsRules,
			List<DroolsGlobalVariable> globalVariables) throws DroolsRuleExecutionException {
		DroolsForm droolsForm = null;
		try {
			if (droolsRules != null && droolsRules.length() > 0) {
				// Launch kie
				KieManager km = new KieManager();
				// Load the rules in memory
				km.buildSessionRules(droolsRules);
				// Creation of the global constants
				km.setGlobalVariables(globalVariables);
				droolsForm = new DroolsForm((SubmittedForm) submittedForm);
				runDroolsRules(droolsForm, km);
			}
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			throw new DroolsRuleExecutionException("Error executing the drools rules", e);
		}
		return droolsForm;
	}
}
