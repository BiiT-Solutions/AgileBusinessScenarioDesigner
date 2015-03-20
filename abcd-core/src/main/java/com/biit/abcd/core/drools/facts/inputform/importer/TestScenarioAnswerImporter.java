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
import com.biit.form.TreeObject;
import com.biit.form.submitted.ISubmiitedGroup;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;

public class TestScenarioAnswerImporter {

	private TestScenarioValidator testValidator = null;

	public ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario) {
		ISubmittedForm submittedForm = null;
		if ((form != null) && (testScenario != null)) {
			// If the test scenario is a subset of the form passed, we parse the
			// structure
			testValidator = new TestScenarioValidator();
			testValidator.checkAndModifyTestScenarioStructure(form, testScenario);
			TestScenarioForm testForm = testScenario.getTestScenarioForm();
			// The name of the form is not longer used and has a static value,
			// so we have to use the label
			submittedForm = new DroolsSubmittedForm(testForm.getLabel());
			// Get the categories
			List<TreeObject> categories = testForm.getChildren();
			if (categories != null) {
				for (TreeObject category : categories) {
					createCategory((TestScenarioCategory) category, submittedForm);
				}
			}
		}
		createSubmittedFormFile(submittedForm);
		return submittedForm;
	}

	private void createCategory(TestScenarioCategory testCategory, ISubmittedForm submittedForm) {
		// Add the category to the submittedForm
		ISubmittedCategory category = new DroolsSubmittedCategory(testCategory.getName());
		submittedForm.addChild(category);
		category.setParent(submittedForm);
		// Put category children variables
		List<TreeObject> categoryChildren = testCategory.getChildren();
		if (categoryChildren != null) {
			for (TreeObject categoryChild : categoryChildren) {
				if (categoryChild instanceof TestScenarioQuestion) {
					createQuestionVariables((TestScenarioQuestion) categoryChild, category);
				} else {
					if (categoryChild instanceof TestScenarioGroup) {
						createGroupVariables((TestScenarioGroup) categoryChild, category);
					}
				}
			}
		}
	}

	private void createGroupVariables(TestScenarioGroup testScenarioGroup, ISubmittedObject parent) {
		ISubmiitedGroup group = new DroolsSubmittedGroup(testScenarioGroup.getName());
		parent.addChild(group);
		group.setParent(parent);
		List<TreeObject> groupChildren = testScenarioGroup.getChildren();
		if (groupChildren != null) {

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof TestScenarioQuestion) {
					createQuestionVariables((TestScenarioQuestion) groupChild, group);
				} else {
					if (groupChild instanceof TestScenarioGroup) {
						createGroupVariables((TestScenarioGroup) groupChild, group);
					}
				}
			}
		}
	}

	private void createQuestionVariables(TestScenarioQuestion testScenarioQuestion, ISubmittedObject parent) {
		ISubmittedQuestion question = new DroolsSubmittedQuestion(testScenarioQuestion.getName());
		setQuestionAnswer(testScenarioQuestion, question);
		parent.addChild(question);
		question.setParent(parent);
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
}
