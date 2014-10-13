package com.biit.abcd.core.drools.facts.inputform.importer;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.form.TreeObject;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;

public class TestScenarioAnswerImporter {

	public static ISubmittedForm createSubmittedForm(Form form, TestScenario testScenario) {
		ISubmittedForm submittedForm = null;
		if ((form != null) && (testScenario != null)) {
			submittedForm = new SubmittedForm(form.getName());

			// Get the categories
			List<TreeObject> categories = form.getChildren();
			if (categories != null) {
				for (TreeObject category : categories) {
					// Add the category to the submittedForm
					ICategory iCategory = new Category(category.getName());
					iCategory.setText(category.getName());
					submittedForm.addCategory(iCategory);
					// Put category children variables
					List<TreeObject> categoryChildren = category.getChildren();
					if (categoryChildren != null) {
						for (TreeObject categoryChild : categoryChildren) {
							if (categoryChild instanceof Group) {
								IGroup iGroup = new com.biit.abcd.core.drools.facts.inputform.Group(
										categoryChild.getName());
								iCategory.addGroup(iGroup);
								List<TreeObject> groupChildren = categoryChild.getChildren();
								if (groupChildren != null) {

									for (TreeObject groupChild : groupChildren) {
										if (groupChild instanceof Group) {
											createNestedGroupVariables((Group) groupChild, iGroup, testScenario);

										} else if (groupChild instanceof Question) {
											IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(
													groupChild.getName());
											setQuestionAnswer((Question) groupChild, iQuestion, testScenario);
											iGroup.addQuestion(iQuestion);
										}
									}
								}
							} else if (categoryChild instanceof Question) {
								IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(
										categoryChild.getName());
								setQuestionAnswer((Question) categoryChild, iQuestion, testScenario);
								iCategory.addQuestion(iQuestion);
							}
						}
					}
				}
			}
		}
		return submittedForm;
	}

	private static void createNestedGroupVariables(TreeObject group, IGroup parentGroup, TestScenario testScenario) {
		IGroup iGroup = new com.biit.abcd.core.drools.facts.inputform.Group(group.getName());
		parentGroup.addGroup(iGroup);
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof Group) {
					createNestedGroupVariables((Group) groupChild, iGroup, testScenario);

				} else if (groupChild instanceof Question) {
					IQuestion iQuestion = new com.biit.abcd.core.drools.facts.inputform.Question(groupChild.getName());
					setQuestionAnswer((Question) groupChild, iQuestion, testScenario);
					iGroup.addQuestion(iQuestion);
				}
			}
		}
	}

	private static void setQuestionAnswer(Question question, IQuestion iQuestion, TestScenario testScenario) {
		TestAnswer testAnswer = testScenario.getTestAnswer((Question) question);
		if ((testAnswer != null) && (testAnswer.getValue() != null)) {
			// We have to separate the set
			// of values to copy the
			// behavior of the orbeon
			// importer
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
