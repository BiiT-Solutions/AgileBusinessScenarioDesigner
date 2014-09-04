package com.biit.abcd.core.drools;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Assert;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.FormParser;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.orbeon.OrbeonCategoryTranslator;
import com.biit.orbeon.OrbeonImporter;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;

public class FormToDroolsExporter {

	private KieManager km;
	// To store the info of the submitted form
	private ISubmittedForm submittedForm;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();
	private String droolsRules = "";

	/**
	 * Parses the vaadin form and loads the rules generated in the drools
	 * engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly
	 * defined. <br>
	 * This method doesn't create any global variables
	 *
	 * @param form
	 *            form to be parsed
	 * @throws ExpressionInvalidException
	 * @throws RuleInvalidException
	 * @throws IOException
	 * @throws RuleNotImplementedException
	 */
	public void parse(Form form) throws ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException {
		if ((form != null) && !form.getChildren().isEmpty()) {
			this.km = new KieManager();
			FormParser formRules;
			try {
				// Creation of the rules
				formRules = new FormParser(form);
				this.droolsRules = formRules.getRules();
				// System.out.println(formRules.getRules());
//				Files.write(Paths.get("./src/test/generatedRules.drl"), formRules.getRules().getBytes());
				// Load the rules in memory
				this.km.buildSessionRules(formRules.getRules());

			} catch (ExpressionInvalidException e) {
				throw e;
			}
		}
	}

	/**
	 * Parses the vaadin form and loads the rules generated in the drools
	 * engine. <br>
	 * If this method doesn't fails it means that the drools rules are correctly
	 * defined. <br>
	 * This method creates the global constants defined in the globalVariables
	 * array
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
	public void parse(Form form, List<GlobalVariable> globalVariables) throws ExpressionInvalidException,
			RuleInvalidException, IOException, RuleNotImplementedException {
		if (!form.getChildren().isEmpty()) {
			this.km = new KieManager();
			FormParser formRules;
			try {
				// Creation of the rules
				formRules = new FormParser(form, globalVariables);
				this.droolsRules = formRules.getRules();
				AbcdLogger.debug(this.getClass().getName(), formRules.getRules());
				// Files.write(Paths.get("./src/test/resources/generatedRules.drl"),
				// formRules.getRules().getBytes());
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
	 * Loads the (Submitted) form as facts of the knowledge base of the drools
	 * engine. <br>
	 * It also starts the engine execution by firing all the rules inside the
	 * engine.
	 *
	 * @param form
	 */
	public void runDroolsRules(ISubmittedForm form) {
		if ((form != null) && (this.km != null)) {
			this.km.setFacts(Arrays.asList(form));
			this.km.execute();
		}
	}

	public void readXml(String formInfo) throws DocumentException, IOException {
		// [0]=App name, [1]=Form name, [2]=Doc id
		String[] infoArray = formInfo.split("::");
		this.submittedForm = new SubmittedForm(infoArray[0], infoArray[1]);
		this.orbeonImporter
				.readXml(OrbeonImporter.getXml(infoArray[0], infoArray[1], infoArray[2]), this.submittedForm);
		Assert.assertNotNull(this.submittedForm);
		Assert.assertFalse(this.submittedForm.getCategories().isEmpty());
	}

	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		// Load the structure file of the form
		// String xmlStructure =
		// readFile(FileReader.getResource("dhszwStructure.xhtml").getAbsolutePath(),
		// Charset.defaultCharset());
		// OrbeonCategoryTranslator.getInstance().readXml(this.submittedForm,
		// xmlStructure);
		OrbeonCategoryTranslator.getInstance().readXml(this.submittedForm);
	}

	public ISubmittedForm submittedForm(Form vaadinForm, List<GlobalVariable> globalVariables, String formInfo)
			throws ExpressionInvalidException, NotValidOperatorInExpression, RuleInvalidException, IOException,
			CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException {
		// Load the submitted form
		this.parse(vaadinForm);
		this.readXml(formInfo);
		this.translateFormCategories();
		this.runDroolsRules(this.submittedForm);
		return this.submittedForm;
	}

	public String getGeneratedRules() {
		return this.droolsRules;
	}
}
