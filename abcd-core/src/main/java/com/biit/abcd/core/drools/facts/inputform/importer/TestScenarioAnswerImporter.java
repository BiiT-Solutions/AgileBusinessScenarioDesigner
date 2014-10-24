package com.biit.abcd.core.drools.facts.inputform.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioObject;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestionAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioRepeatedGroupContainer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;

public class TestScenarioAnswerImporter {

	public static ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario)
			throws ChildrenNotFoundException {
//		ISubmittedForm submittedForm = null;
//		if ((form != null) && (testScenario != null)) {
//			// If the test scenario is a subset of the form passed, we parse the
//			// structure
//			if (TestScenarioValidator.isSubset(form, testScenario.getTestScenarioForm())) {
//				// setTestScenario(testScenario);
//				TestScenarioObject testForm = testScenario.getTestScenarioForm();
//				submittedForm = new SubmittedForm(testForm.getName());
//				// Get the categories
//				List<TreeObject> categories = testForm.getChildren();
//				if (categories != null) {
//					for (TreeObject category : categories) {
//						createCategory((TestScenarioObject) category, submittedForm);
//					}
//				}
//
//			}
//		}
//		createSubmittedFormFile(submittedForm);
		return null;
		// return submittedForm;
	}

	private static void createCategory(TestScenarioObject testCategory, ISubmittedForm submittedForm) {
		// Add the category to the submittedForm
		ICategory iCategory = new com.biit.abcd.core.drools.facts.inputform.Category(testCategory.getXmlTag());
		submittedForm.addCategory(iCategory);
		// Put category children variables
		List<TreeObject> categoryChildren = testCategory.getChildren();
		if (categoryChildren != null) {
			for (TreeObject categoryChild : categoryChildren) {
				if (categoryChild instanceof TestScenarioQuestionAnswer) {
					createQuestionVariables((TestScenarioQuestionAnswer) categoryChild, iCategory);
				} else {
					if (categoryChild instanceof TestScenarioRepeatedGroupContainer) {
						TestScenarioRepeatedGroupContainer repeatedGroupContainer = (TestScenarioRepeatedGroupContainer) categoryChild;
						for (TreeObject repeatedGroup : repeatedGroupContainer.getChildren()) {
							createGroupVariables((TestScenarioObject) repeatedGroup, iCategory);
						}
					} else {
						createGroupVariables((TestScenarioObject) categoryChild, iCategory);
					}

				}
			}
		}
	}

	private static void createGroupVariables(TestScenarioObject testGroup, IGroup parentGroup) {
		IGroup iGroup = new com.biit.abcd.core.drools.facts.inputform.Group(testGroup.getXmlTag());
		parentGroup.addGroup(iGroup);
		List<TreeObject> groupChildren = testGroup.getChildren();
		if (groupChildren != null) {

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof TestScenarioQuestionAnswer) {
					createQuestionVariables((TestScenarioQuestionAnswer) groupChild, iGroup);
				} else {
					if (groupChild instanceof TestScenarioRepeatedGroupContainer) {
						TestScenarioRepeatedGroupContainer repeatedGroupContainer = (TestScenarioRepeatedGroupContainer) groupChild;
						for (TreeObject repeatedGroup : repeatedGroupContainer.getChildren()) {
							createGroupVariables((TestScenarioObject) repeatedGroup, iGroup);
						}
					} else {
						createGroupVariables((TestScenarioObject) groupChild, iGroup);
					}
				}
			}
		}
	}

	private static void createQuestionVariables(TestScenarioQuestionAnswer treeObject, IGroup parentGroup) {
		IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(treeObject.getXmlTag());
		setQuestionAnswer(treeObject, iQuestion);
		parentGroup.addQuestion(iQuestion);
	}

	private static void setQuestionAnswer(TestScenarioQuestionAnswer testQuestion, IQuestion iQuestion) {
		TestAnswer testAnswer = testQuestion.getTestAnswer();
		if ((testAnswer != null) && (testAnswer.getValue() != null)) {
			// We have to separate the set of values to copy the behavior of the
			// orbeon importer
			if (testAnswer instanceof TestAnswerMultiCheckBox) {
				Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
				String valueSet = "";
				for (String value : values) {
					valueSet += value + " ";
				}
				// Remove the last space
				valueSet = valueSet.substring(0, valueSet.length() - 2);
				iQuestion.setAnswer(valueSet);
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

	private static void createSubmittedFormFile(ISubmittedForm submittedForm) {
		if (submittedForm != null) {
			try {
				String submmitedFormXml = ((SubmittedForm) submittedForm).generateXML();
				Files.write(
						Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "submittedFormGenerated.xml"),
						submmitedFormXml.getBytes("UTF-8"));
			} catch (IOException e) {
				AbcdLogger.errorMessage(TestScenarioAnswerImporter.class.getName(), e);
			}
		}
	}
}
