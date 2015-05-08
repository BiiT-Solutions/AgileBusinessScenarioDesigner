package com.biit.abcd.core.drools.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.drools.KieManager;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.importer.OrbeonSubmittedAnswerImporter;
import com.biit.form.submitted.ISubmittedForm;

/**
 * Tests the rule loading from a static file<br>
 * Needs the files kidScreen.xml and droolsRulesFileTest.drl in test/resources
 */
public class DroolsEngineRulesTest {

	private final static String APP = "Application1";
	private final static String FORM_NAME = "Form1";
	private ISubmittedForm submittedForm;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	private void createSubmittedForm() {
		try {
			setSubmittedForm(new DroolsSubmittedForm(APP, FORM_NAME));
			String xmlFile = readFile("./src/test/resources/kidScreen.xml", StandardCharsets.UTF_8);
			getOrbeonImporter().readXml(xmlFile, getSubmittedForm());
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private DroolsForm runDroolsRules(String drlFile) {
		// Generate the drools rules.
		try {
			AbcdLogger.debug(this.getClass().getName(), drlFile);
			Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedRules.drl"),
					drlFile.getBytes("UTF-8"));
			createSubmittedForm();
			// Launch kie
			KieManager km = new KieManager();
			// Load the rules in memory
			km.buildSessionRules(drlFile);
			// Creation of the global constants
			km.setGlobalVariables(null);
			DroolsForm droolsForm = new DroolsForm(getSubmittedForm());
			km.setFacts(Arrays.asList((ISubmittedForm) droolsForm));
			km.execute();
			return droolsForm;
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private ISubmittedForm getSubmittedForm() {
		return submittedForm;
	}

	private void setSubmittedForm(ISubmittedForm submittedForm) {
		this.submittedForm = submittedForm;
	}

	private OrbeonSubmittedAnswerImporter getOrbeonImporter() {
		return orbeonImporter;
	}

	@Test(groups = { "droolsEngineRules" })
	public void rulesTest() {
		try {
			String drlFile = readFile("./src/test/resources/droolsRulesFileTest.drl", StandardCharsets.UTF_8);
			// Execution of the rules
			DroolsForm droolsForm = runDroolsRules(drlFile);
			if (submittedForm != null) {
				// Check result
				Assert.assertEquals(
						((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue("customVariableResult"),
						11.);
			}
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}
}
