package com.biit.abcd.core.drools.facts.inputform.importer;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.form.TreeObject;
import com.biit.orbeon.form.ISubmittedForm;

// TODO fix importer to admit repeatable groups
public class TestScenarioAnswerImporter {

	public static TestScenario testScenario = null;

	private static void setTestScenario(TestScenario externalTestScenario) {
		testScenario = externalTestScenario;
	}

	private static TestScenario getTestScenario() {
		return testScenario;
	}

//	private static void checkFormTestScenarioStructure(Form form, TestScenario testScenario) {
//		testScenario.getTestScenarioForm()
//	}

	public static ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario) {
		ISubmittedForm submittedForm = null;
		if ((form != null) && (testScenario != null)) {

//			checkFormTestScenarioStructure(form, testScenario);

			setTestScenario(testScenario);
			submittedForm = new SubmittedForm(form.getName());
			// Get the categories
			List<TreeObject> categories = form.getChildren();
			if (categories != null) {
				for (TreeObject category : categories) {
					// createCategory(category, submittedForm);
				}
			}
		}
		return submittedForm;
	}

	// private static void createCategory(TreeObject category, ISubmittedForm
	// submittedForm) {
	// // Add the category to the submittedForm
	// ICategory iCategory = new Category(category.getName());
	// iCategory.setText(category.getName());
	// submittedForm.addCategory(iCategory);
	// // Put category children variables
	// List<TreeObject> categoryChildren = category.getChildren();
	// if (categoryChildren != null) {
	// for (TreeObject categoryChild : categoryChildren) {
	// if (categoryChild instanceof Group) {
	// IGroup iGroup = new
	// com.biit.abcd.core.drools.facts.inputform.Group(categoryChild.getName());
	// iCategory.addGroup(iGroup);
	// List<TreeObject> groupChildren = categoryChild.getChildren();
	// if (groupChildren != null) {
	//
	// for (TreeObject groupChild : groupChildren) {
	// if (groupChild instanceof Group) {
	// createNestedGroupVariables((Group) groupChild, iGroup);
	//
	// } else if (groupChild instanceof Question) {
	// IQuestion iQuestion = new
	// com.biit.abcd.core.drools.facts.inputform.Question(
	// groupChild.getName());
	// setQuestionAnswer((Question) groupChild, iQuestion);
	// iGroup.addQuestion(iQuestion);
	// }
	// }
	// }
	// } else if (categoryChild instanceof Question) {
	// IQuestion iQuestion = new
	// com.biit.abcd.core.drools.facts.inputform.Question(
	// categoryChild.getName());
	// // The questionIndex is 0 because a category can't have
	// // several equal questions (Parameter only relevant for
	// // repeatable groups)
	// setQuestionAnswer((Question) categoryChild, iQuestion, 0);
	// iCategory.addQuestion(iQuestion);
	// }
	// }
	// }
	// }
	//
	// private static void createGroup() {
	// }
	//
	// private static void createNestedGroupVariables(TreeObject group, IGroup
	// parentGroup) {
	// IGroup iGroup = new
	// com.biit.abcd.core.drools.facts.inputform.Group(group.getName());
	// parentGroup.addGroup(iGroup);
	// List<TreeObject> groupChildren = group.getChildren();
	// if (groupChildren != null) {
	//
	// for (TreeObject groupChild : groupChildren) {
	// if (groupChild instanceof Group) {
	// createNestedGroupVariables((Group) groupChild, iGroup);
	//
	// } else if (groupChild instanceof Question) {
	// IQuestion iQuestion = new
	// com.biit.abcd.core.drools.facts.inputform.Question(groupChild.getName());
	// setQuestionAnswer((Question) groupChild, iQuestion);
	// iGroup.addQuestion(iQuestion);
	// }
	// }
	// }
	// }
	//
	// private static void setQuestionAnswer(Question question, IQuestion
	// iQuestion, int repeatedQuestionIndex) {
	// TestAnswer testAnswer =
	// getTestScenario().getTestAnswerList(question).getTestAnswer(repeatedQuestionIndex);
	// if ((testAnswer != null) && (testAnswer.getValue() != null)) {
	// // We have to separate the set
	// // of values to copy the
	// // behavior of the orbeon
	// // importer
	// if (testAnswer instanceof TestAnswerMultiCheckBox) {
	// Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
	// String valueSet = "";
	// for (String value : values) {
	// valueSet += value + " ";
	// }
	// // Remove the last space
	// valueSet = valueSet.substring(0, valueSet.length() - 2);
	// iQuestion.setAnswer(valueSet);
	// }
	// // Transform the timestamp to a date
	// else if (testAnswer instanceof TestAnswerInputDate) {
	// Timestamp timeStamp = ((TestAnswerInputDate) testAnswer).getValue();
	// Date date = new Date(timeStamp.getTime());
	// iQuestion.setAnswer(date.toString());
	// } else {
	// iQuestion.setAnswer(testAnswer.getValue().toString());
	// }
	// } else {
	// iQuestion.setAnswer("");
	// }
	// }
}
