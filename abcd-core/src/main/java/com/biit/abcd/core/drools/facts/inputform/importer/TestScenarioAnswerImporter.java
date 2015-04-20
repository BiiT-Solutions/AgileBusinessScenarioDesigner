package com.biit.abcd.core.drools.facts.inputform.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.drools.facts.inputform.importer.validator.TestScenarioValidator;
import com.biit.abcd.core.drools.facts.inputform.importer.validator.TestScenarioValidatorMessage;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;

public class TestScenarioAnswerImporter {

	private TestScenarioValidator testValidator = null;
	private ISubmittedForm submittedForm;

	public ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario) {
		if ((form != null) && (testScenario != null)) {
			// If the test scenario is a subset of the form passed, we parse the
			// structure
			testValidator = new TestScenarioValidator();
			testValidator.checkAndModifyTestScenarioStructure(form, testScenario);
			TestScenarioForm testForm = testScenario.getTestScenarioForm();
			createDroolsSubmittedForm(testForm);
		}
		createSubmittedFormFile(getSubmitedForm());
		return getSubmitedForm();
	}

	private void createSubmittedFormFile(ISubmittedForm submittedForm) {
		if (submittedForm != null) {
			try {
				String submmitedFormXml = ((DroolsSubmittedForm) submittedForm).generateXML();
				Files.write(
						Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedSubmittedForm.xml"),
						submmitedFormXml.getBytes("UTF-8"));
			} catch (IOException e) {
				AbcdLogger.errorMessage(TestScenarioAnswerImporter.class.getName(), e);
			}
		}
	}

	public List<TestScenarioValidatorMessage> getTestScenarioModifications() {
		return testValidator.getValidatorMessages();
	}

	private void createDroolsSubmittedForm(BaseForm form) {
		setSubmittedForm(new DroolsSubmittedForm(form.getLabel(), form.getVersion().toString()));
		createSubmittedFromStructure(form, getSubmitedForm());
	}

	private void createSubmittedFromStructure(TreeObject treeObject, ISubmittedObject parent) {
		for (TreeObject child : treeObject.getChildren()) {
			if (child instanceof TestScenarioCategory) {
				ISubmittedObject droolsCategory = createCategory(parent, child.getName());
				createSubmittedFromStructure(child, droolsCategory);
			} else if (child instanceof TestScenarioGroup) {
				ISubmittedObject droolsGroup = createGroup(parent, child.getName());
				createSubmittedFromStructure(child, droolsGroup);
			} else if (child instanceof TestScenarioQuestion) {
				ISubmittedQuestion question = (ISubmittedQuestion) createQuestion(parent, child.getName());
				setQuestionAnswer(((TestScenarioQuestion) child), question);
			}
		}
	}

	private ISubmittedObject createCategory(ISubmittedObject parent, String tag) {
		ISubmittedCategory category = new DroolsSubmittedCategory(tag);
		parent.addChild(category);
		return category;
	}

	private ISubmittedObject createGroup(ISubmittedObject parent, String tag) {
		ISubmittedGroup group = new DroolsSubmittedGroup(tag);
		parent.addChild(group);
		return group;
	}

	private ISubmittedObject createQuestion(ISubmittedObject parent, String tag) {
		ISubmittedQuestion question = new DroolsSubmittedQuestion(tag);
		parent.addChild(question);
		return question;
	}

	private void setQuestionAnswer(TestScenarioQuestion testScenarioQuestion, ISubmittedQuestion iQuestion) {
		TestAnswer testAnswer = testScenarioQuestion.getTestAnswer();
		if ((testAnswer != null) && (testAnswer.getValue() != null)) {
			// We have to separate the set of values to copy the behavior of the
			// orbeon importer
			if (testAnswer instanceof TestAnswerMultiCheckBox) {
				Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
				if (!values.isEmpty()) {
					String valueSet = "";
					for (String value : values) {
						valueSet += value + " ";
					}
					// Remove the last space
					valueSet = valueSet.substring(0, valueSet.length() - 2);
					iQuestion.setAnswer(valueSet);
				}
			}
			// Transform the timestamp to a date
			else if (testAnswer instanceof TestAnswerInputDate) {
				Timestamp timeStamp = ((TestAnswerInputDate) testAnswer).getValue();
				Date date = new Date(timeStamp.getTime());
				iQuestion.setAnswer(date.toString());
			} else {
				iQuestion.setAnswer(testAnswer.getValue().toString());
			}
		} else {
			iQuestion.setAnswer("");
		}
	}

	public ISubmittedForm getSubmitedForm() {
		return submittedForm;
	}

	private void setSubmittedForm(ISubmittedForm submittedForm) {
		this.submittedForm = submittedForm;
	}
}
