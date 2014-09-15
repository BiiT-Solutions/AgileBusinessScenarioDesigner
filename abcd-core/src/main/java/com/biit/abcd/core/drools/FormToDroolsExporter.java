package com.biit.abcd.core.drools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Assert;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.orbeon.OrbeonCategoryTranslator;
import com.biit.orbeon.OrbeonImporter;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;

public class FormToDroolsExporter {

	/**
	 * Parses the vaadin form and loads the rules generated in the drools engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly defined. <br>
	 * This method creates the global constants defined in the globalVariables array
	 * 
	 * @param form
	 *            form to be parsed
	 * @param globalVariables
	 *            array with the global constants to be created
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 * @throws IOException
	 * @throws RuleNotImplementedException
	 */
	public DroolsRulesGenerator generateDroolRules(Form form, List<GlobalVariable> globalVariables)
			throws ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException {
		if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
			DroolsRulesGenerator formRules;
			try {
				// Creation of the rules
				formRules = new DroolsRulesGenerator(form, globalVariables);
				AbcdLogger.debug(this.getClass().getName(), formRules.getRules());
				Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedRules.drl"),
						formRules.getRules().getBytes());
				return formRules;
			} catch (ExpressionInvalidException e) {
				throw e;
			}
		}
		return null;
	}

	/**
	 * Loads the (Submitted) form as facts of the knowledge base of the drools engine. <br>
	 * It also starts the engine execution by firing all the rules inside the engine.
	 * 
	 * @param form
	 */
	public void runDroolsRules(ISubmittedForm form, KieManager km) {
		if ((form != null) && (km != null)) {
			km.setFacts(Arrays.asList(form));
			km.execute();
		}
	}

	private ISubmittedForm readXml(String orbeonApplicationName, String orbeonFormName, String orbeonDocumentId)
			throws DocumentException, IOException {
		ISubmittedForm submittedForm = new SubmittedForm(orbeonApplicationName, orbeonFormName);

		OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();
		orbeonImporter.readXml(OrbeonImporter.getXml(orbeonApplicationName, orbeonFormName, orbeonDocumentId),
				submittedForm);
		Assert.assertNotNull(submittedForm);
		Assert.assertFalse(submittedForm.getCategories().isEmpty());
		return submittedForm;
	}

	public void translateFormCategories(ISubmittedForm submittedForm) throws DocumentException,
			CategoryNameWithoutTranslation, IOException {
		OrbeonCategoryTranslator.getInstance().readXml(submittedForm);
	}

	public ISubmittedForm processForm(Form form, String orbeonApplicationName, String orbeonFormName,
			String orbeonDocumentId) throws ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation {
		// Generate all drools rules.
		DroolsRulesGenerator rulesGenerator = generateDroolRules(form, null);
		// Obtain results
		if (rulesGenerator != null) {
			return applyDrools(orbeonApplicationName, orbeonFormName, orbeonDocumentId, rulesGenerator.getRules(),
					rulesGenerator.getGlobalVariables());
		} else
			return null;
	}

	public ISubmittedForm applyDrools(String orbeonApplicationName, String orbeonFormName, String orbeonDocumentId,
			String droolsRules, List<DroolsGlobalVariable> globalVariables) throws DocumentException, IOException,
			CategoryNameWithoutTranslation {
		ISubmittedForm submittedForm = readXml(orbeonApplicationName, orbeonFormName, orbeonDocumentId);
		translateFormCategories(submittedForm);
		ISubmittedForm droolsForm = new DroolsForm((SubmittedForm) submittedForm);
		if (droolsRules != null && droolsRules.length() > 0) {
			// Launch kie
			KieManager km = new KieManager();
			// Load the rules in memory
			km.buildSessionRules(droolsRules);
			// Creation of the global constants
			km.setGlobalVariables(globalVariables);

			runDroolsRules(droolsForm, km);
		}
		return droolsForm;
	}

	/**
	 * Method used for testing purposes.<br>
	 * 
	 * @param submittedForm without scores
	 * @param droolsRules
	 * @param globalVariables
	 * @return submittedForm with the scores calculated by drools
	 * @throws DocumentException
	 * @throws IOException
	 * @throws CategoryNameWithoutTranslation
	 */
	public ISubmittedForm applyDrools(ISubmittedForm submittedForm, String droolsRules,
			List<DroolsGlobalVariable> globalVariables) throws DocumentException, IOException,
			CategoryNameWithoutTranslation {
		// Launch kie
		KieManager km = new KieManager();
		// Load the rules in memory
		km.buildSessionRules(droolsRules);
		// km.buildSessionRules(new
		// String(Files.readAllBytes(Paths.get(System.getProperty("java.io.tmpdir")
		// + File.separator + "generatedRules.drl"))));
		// Creation of the global constants
		km.setGlobalVariables(globalVariables);
		ISubmittedForm droolsForm = new DroolsForm((SubmittedForm) submittedForm);
		runDroolsRules(droolsForm, km);
		return droolsForm;
	}
}
