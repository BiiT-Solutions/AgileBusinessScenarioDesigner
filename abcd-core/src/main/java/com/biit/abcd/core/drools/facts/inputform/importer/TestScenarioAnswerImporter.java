package com.biit.abcd.core.drools.facts.inputform.importer;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioObject;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestionAnswer;
import com.biit.form.TreeObject;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;

// TODO fix importer to admit repeatable groups
public class TestScenarioAnswerImporter {

	private static TestScenario testScenario = null;
	private static Map<String, TestScenarioQuestionAnswer> testScenarioQuestionMap;

	private static void setTestScenario(TestScenario externalTestScenario) {
		testScenario = externalTestScenario;
		testScenarioQuestionMap = new HashMap<String, TestScenarioQuestionAnswer>();
		createTestScenarioQuestionMap(testScenario.getTestScenarioForm());
	}

	private static void createTestScenarioQuestionMap(TestScenarioObject testScenarioObject) {
		for (TreeObject child : testScenarioObject.getChildren()) {
			if (child instanceof TestScenarioQuestionAnswer) {
				testScenarioQuestionMap.put(child.getName(), (TestScenarioQuestionAnswer) child);
			} else {
				createTestScenarioQuestionMap((TestScenarioObject) child);
			}
		}
	}

	private static TestScenario getTestScenario() {
		return testScenario;
	}

	private static TreeObject findChild(TreeObject treeObject, TestScenarioObject testScenarioObject) {
		for (TreeObject child : treeObject.getChildren()) {
			if (child.getUniqueNameReadable().equals(testScenarioObject.getName())) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Checks if testScenarioObject is a subset of the treeObject structure. To
	 * be a subset all elements of testScenarioObject and their children need to
	 * be present in current structure.
	 * 
	 */
	private static boolean isSubset(TreeObject treeObject, TestScenarioObject testScenarioObject) {
		for (TreeObject child : testScenarioObject.getChildren()) {
			// Find a child of this group with the same name as the children
			TreeObject thisCorrespondence = findChild(treeObject, (TestScenarioObject) child);
			if (thisCorrespondence == null || (!isSubset(thisCorrespondence, (TestScenarioObject) child))) {
				// If child is not found is not a subset.
				// If child is not a subset of thisCorrespondence is not a
				// subset
				return false;
			}
		}
		// If all the elements have not failed then is a subset.
		return true;
	}

	public static ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario) {
		ISubmittedForm submittedForm = null;
		if ((form != null) && (testScenario != null)) {
			// If the test scenario is a subset of the form passed, we parse the
			// structure
			if (isSubset(form, testScenario.getTestScenarioForm())) {
				setTestScenario(testScenario);
				submittedForm = new SubmittedForm(form.getName());
				// Get the categories
				List<TreeObject> categories = form.getChildren();
				if (categories != null) {
					for (TreeObject category : categories) {
						createCategory(category, submittedForm);
					}
				}

			}
		}
		return submittedForm;
	}

	private static void createCategory(TreeObject category, ISubmittedForm submittedForm) {
		// Add the category to the submittedForm
		ICategory iCategory = new com.biit.abcd.core.drools.facts.inputform.Category(category.getName());
		iCategory.setText(category.getName());
		submittedForm.addCategory(iCategory);
		// Put category children variables
		List<TreeObject> categoryChildren = category.getChildren();
		if (categoryChildren != null) {
			for (TreeObject categoryChild : categoryChildren) {
				if (categoryChild instanceof Group) {
					IGroup iGroup = new com.biit.abcd.core.drools.facts.inputform.Group(categoryChild.getName());
					iCategory.addGroup(iGroup);
					List<TreeObject> groupChildren = categoryChild.getChildren();
					if (groupChildren != null) {

						for (TreeObject groupChild : groupChildren) {
							if (groupChild instanceof Group) {
								createNestedGroupVariables((Group) groupChild, iGroup);

							} else if (groupChild instanceof Question) {
								IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(
										groupChild.getName());
								setQuestionAnswer((Question) groupChild, iQuestion);
								iGroup.addQuestion(iQuestion);
							}
						}
					}
				} else if (categoryChild instanceof Question) {
					IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(
							categoryChild.getName());

					setQuestionAnswer((Question) categoryChild, iQuestion);
					iCategory.addQuestion(iQuestion);
				}
			}
		}
	}

	private static void createNestedGroupVariables(TreeObject group, IGroup parentGroup) {
		IGroup iGroup = new com.biit.abcd.core.drools.facts.inputform.Group(group.getName());
		parentGroup.addGroup(iGroup);
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof Group) {
					createNestedGroupVariables((Group) groupChild, iGroup);

				} else if (groupChild instanceof Question) {
					IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(groupChild.getName());
					setQuestionAnswer((Question) groupChild, iQuestion);
					iGroup.addQuestion(iQuestion);
				}
			}
		}
	}

	private static void setQuestionAnswer(Question question, IQuestion iQuestion) {
		TestAnswer testAnswer = testScenarioQuestionMap.get(question.getUniqueNameReadable()).getTestAnswer();
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
}
