package com.biit.abcd.core.drools.facts.inputform.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputPostalCode;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputText;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerRadioButton;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class TestScenarioValidator {

	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private TestScenario testScenarioAnalyzed;
	private List<String> scenarioModifications;
	private List<TreeObject> testScenarioChildren;

	/**
	 * Compares the test scenario structure against the form structure and
	 * modifies the objects needed
	 * 
	 * @param form
	 * @param testScenario
	 */
	public void checkAndModifyTestScenarioStructure(Form form, TestScenario testScenario) {
		if (form != null && testScenario != null) {
			scenarioModifications = new ArrayList<String>();
			testScenarioAnalyzed = testScenario;
			// First check the changes in the structure already existent
			originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
			TestScenarioForm testScenarioForm = testScenario.getTestScenarioForm();
			if (originalReferenceTreeObjectMap.containsKey(testScenarioForm.getOriginalReference())) {
				List<TreeObject> children = testScenarioForm.getChildren();
				for (int i = 0; i < children.size(); i++) {
					TreeObject treeObject = children.get(i);
					if (treeObject instanceof TestScenarioCategory) {
						checkTestScenarioCategoryStructure((TestScenarioCategory) treeObject);
					}
				}
			}
			testScenarioChildren = testScenario.getTestScenarioForm().getAll(TreeObject.class);
			for (TreeObject treeObject : form.getChildren()) {
				checkRemainingFormStructure(treeObject, testScenario.getTestScenarioForm());
			}
		}
	}

	private void checkTestScenarioCategoryStructure(TestScenarioCategory testScenarioCategory) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioCategory.getOriginalReference())) {
				getScenarioModifications().add("Category '" + testScenarioCategory.getName() + "' deleted.");
				testScenarioCategory.remove();
			} else {
				TreeObject category = originalReferenceTreeObjectMap.get(testScenarioCategory.getOriginalReference());
				if (!category.getName().equals(testScenarioCategory.getName())) {
					String oldName = testScenarioCategory.getName();
					testScenarioCategory.setName(category.getName());
					getScenarioModifications().add(
							"Category name changed from '" + oldName + "' to '" + category.getName() + "'");
				}
				List<TreeObject> children = testScenarioCategory.getChildren();
				for (int i = 0; i < children.size(); i++) {
					TreeObject treeObject = children.get(i);
					if (treeObject instanceof TestScenarioGroup) {
						checkTestScenarioGroupStructure((TestScenarioGroup) treeObject);

					} else if (treeObject instanceof TestScenarioQuestion) {
						checkTestScenarioQuestionStructure((TestScenarioQuestion) treeObject);
					}
				}
			}
		} catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private void checkTestScenarioGroupStructure(TestScenarioGroup testScenarioGroup) {
		try {
			// If the group doesn't exists in the original form
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioGroup.getOriginalReference())) {
				getScenarioModifications().add("Group '" + testScenarioGroup.getName() + "' deleted.");
				testScenarioGroup.remove();
			} else {
				TreeObject group = originalReferenceTreeObjectMap.get(testScenarioGroup.getOriginalReference());
				if (group.getPathName().equals(testScenarioGroup.getPathName())) {
					// If the group exists but has different attributes
					if ((group instanceof Group)) {
						if (!testScenarioGroup.isRepeatable() && (((Group) group).isRepeatable())) {
							testScenarioGroup.setRepeatable(true);
							getScenarioModifications().add(
									"Group '" + testScenarioGroup.getName()
											+ "' set to repeatable - Test scenario group not repeatable, Form group repeatable");

						} else if (testScenarioGroup.isRepeatable() && !((Group) group).isRepeatable()) {
							keepFirstBornGroup(testScenarioGroup);
							getScenarioModifications().add(
									"Repeatable groups after the first '" + testScenarioGroup.getName()
											+ "' deleted - Test scenario group repeatable, Form group not repeatable");
						}
					}
				} else {
					// If the group exists but is in a different position
					TreeObject whatToMove = testScenarioGroup;
					TreeObject oldParent = testScenarioGroup.getParent();
					TreeObject whereToMove = testScenarioAnalyzed.getTestScenarioForm()
							.getOriginalReferenceTreeObjectMap().get(group.getParent().getOriginalReference());
					TreeObject.move(whatToMove, whereToMove);
					getScenarioModifications().add(
							"Group '" + testScenarioGroup.getName() + "' moved from parent '" + oldParent.getName()
									+ "' to parent '" + testScenarioGroup.getParent().getName() + "'");

				}
				if (!group.getName().equals(testScenarioGroup.getName())) {
					String oldName = testScenarioGroup.getName();
					testScenarioGroup.setName(group.getName());
					getScenarioModifications().add(
							"Group name changed from '" + oldName + "' to '" + group.getName() + "'");
				}
				List<TreeObject> children = testScenarioGroup.getChildren();
				for (int i = 0; i < children.size(); i++) {
					TreeObject treeObject = children.get(i);
					if (treeObject instanceof TestScenarioGroup) {
						checkTestScenarioGroupStructure((TestScenarioGroup) treeObject);

					} else if (treeObject instanceof TestScenarioQuestion) {
						checkTestScenarioQuestionStructure((TestScenarioQuestion) treeObject);
					}
				}
			}
		} catch (NotValidChildException | FieldTooLongException | CharacterNotAllowedException
				| DependencyExistException | ChildrenNotFoundException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private void checkTestScenarioQuestionStructure(TestScenarioQuestion testScenarioQuestion) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioQuestion.getOriginalReference())) {
				getScenarioModifications().add("Question '" + testScenarioQuestion.getName() + "' deleted.");
				testScenarioQuestion.remove();

			} else {
				TreeObject question = originalReferenceTreeObjectMap.get(testScenarioQuestion.getOriginalReference());
				if (!question.getName().equals(testScenarioQuestion.getName())) {
					String oldName = testScenarioQuestion.getName();
					testScenarioQuestion.setName(question.getName());
					getScenarioModifications().add(
							"Question name changed from '" + oldName + "' to '" + question.getName() + "'");
				}
				checkAnswerType((Question) question, testScenarioQuestion);
			}
		} catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private void keepFirstBornGroup(TestScenarioGroup oldValue) {
		try {
			TreeObject parent = oldValue.getParent();
			// We have to check every possible child in case it is a repeatable
			// group
			Set<TreeObject> childToRemove = new HashSet<TreeObject>();
			for (TreeObject child : parent.getChildren()) {
				if (child.getName().equals(oldValue.getName())) {
					childToRemove.add(child);
				}
			}
			int i = 0;
			// Keep the first child
			for (TreeObject treeObject : childToRemove) {
				if (i != 0) {
					treeObject.remove();
				}
				i++;
			}
			oldValue.setRepeatable(false);

		} catch (DependencyExistException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private void replaceTestScenarioGroup(TestScenarioGroup oldValue, TestScenarioGroup newValue) {
		try {
			TreeObject parent = oldValue.getParent();
			Integer oldChildIndex = parent.getIndex(oldValue);
			// We have to check every possible child in case it is a repeatable
			// group
			Set<TreeObject> childToRemove = new HashSet<TreeObject>();
			for (TreeObject child : parent.getChildren()) {
				if (child.getName().equals(oldValue.getName())) {
					childToRemove.add(child);
				}
			}
			for (TreeObject treeObject : childToRemove) {
				treeObject.remove();
			}
			// After removing the children we add the new child
			parent.addChild(oldChildIndex, newValue);

		} catch (DependencyExistException | NotValidChildException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private void createTestScenarioObject(TreeObject formTreeObject, TreeObject testScenarioTreeObjectParent) {
		try {
			if (formTreeObject instanceof Category) {
				TestScenarioCategory testScenarioCategory = new TestScenarioCategory();
				testScenarioCategory.setOriginalReference(formTreeObject.getOriginalReference());
				testScenarioCategory.setName(formTreeObject.getName());
				testScenarioTreeObjectParent.addChild(testScenarioCategory);
				getScenarioModifications().add("Category '" + testScenarioCategory.getName() + "' created empty.");
				// Copy children
				for (TreeObject treeObject : formTreeObject.getChildren()) {
					createTestScenarioObject(treeObject, testScenarioCategory);
				}
			} else if (formTreeObject instanceof Group) {
				TestScenarioGroup testScenarioGroup = new TestScenarioGroup();
				testScenarioGroup.setOriginalReference(formTreeObject.getOriginalReference());
				testScenarioGroup.setName(formTreeObject.getName());
				testScenarioGroup.setRepeatable(((Group) formTreeObject).isRepeatable());
				testScenarioTreeObjectParent.addChild(testScenarioGroup);
				getScenarioModifications().add("Group '" + testScenarioGroup.getName() + "' created empty.");
				// Copy children
				for (TreeObject treeObject : formTreeObject.getChildren()) {
					createTestScenarioObject(treeObject, testScenarioGroup);
				}
			} else if (formTreeObject instanceof Question) {
				TestScenarioQuestion testScenarioQuestion = new TestScenarioQuestion();
				testScenarioQuestion.setOriginalReference(formTreeObject.getOriginalReference());
				testScenarioQuestion.setName(formTreeObject.getName());
				testScenarioTreeObjectParent.addChild(testScenarioQuestion);
				getScenarioModifications().add("Question '" + testScenarioQuestion.getName() + "' created empty.");
			}
			// Any other tree object type not taken into account
		} catch (FieldTooLongException | CharacterNotAllowedException | NotValidChildException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	/**
	 * Only validates the structure, not modifies the test scenario.
	 * 
	 * @param form
	 * @param testScenario
	 * @return
	 */
	public boolean validateToLaunch(Form form, TestScenario testScenario) {
		originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
		return validateToLaunch(testScenario.getTestScenarioForm());
	}

	private boolean validateToLaunch(TreeObject testScenarioObject) {
		if (originalReferenceTreeObjectMap.containsKey(testScenarioObject.getOriginalReference())) {
			for (TreeObject treeObject : testScenarioObject.getChildren()) {
				if (!validateToLaunch(treeObject)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the test answer matches the question type
	 */
	private void checkAnswerType(Question question, TestScenarioQuestion testScenarioQuestion) {
		TestAnswer oldTestAnswer = testScenarioQuestion.getTestAnswer();
		TestAnswer testAnswer = testScenarioQuestion.getTestAnswer();
		switch (question.getAnswerType()) {
		case RADIO:
			if (!(testAnswer instanceof TestAnswerRadioButton)) {
				TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
				if (newTestScenarioQuestion != null) {
					newTestScenarioQuestion.setTestAnswer(new TestAnswerRadioButton());
					setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
				}
			}
			break;
		case MULTI_CHECKBOX:
			if (!(testAnswer instanceof TestAnswerMultiCheckBox)) {
				TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
				if (newTestScenarioQuestion != null) {
					newTestScenarioQuestion.setTestAnswer(new TestAnswerMultiCheckBox());
					setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
				}
			}
			break;
		case INPUT:
			switch (question.getAnswerFormat()) {
			case TEXT:
				if (!(testAnswer instanceof TestAnswerInputText)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputText());
						setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
					}
				}
				break;
			case POSTAL_CODE:
				if (!(testAnswer instanceof TestAnswerInputPostalCode)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputPostalCode());
						setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
					}
				}
				break;
			case NUMBER:
				if (!(testAnswer instanceof TestAnswerInputNumber)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputNumber());
						setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
					}
				}
				break;
			case DATE:
				if (!(testAnswer instanceof TestAnswerInputDate)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputDate());
						setTestAnswerReplaceMessage(oldTestAnswer, newTestScenarioQuestion.getTestAnswer());
					}
				}
				break;
			}
		}
	}

	private void setTestAnswerReplaceMessage(TestAnswer oldTestAnswer, TestAnswer newTestAnswer) {
		getScenarioModifications().add(
				"Test answer replaced. Old type '" + oldTestAnswer.getClass().getSimpleName() + "', new type '"
						+ newTestAnswer.getClass().getSimpleName() + "'");
	}

	private TestScenarioQuestion replaceTestScenarioQuestion(TestScenarioQuestion testScenarioQuestion) {
		TestScenarioQuestion newTestScenarioQuestion = null;
		try {
			replaceTreeObjectChild(testScenarioQuestion, testScenarioQuestion.copyTestScenarioQuestion());
			getScenarioModifications().add("Question '" + testScenarioQuestion.getName() + "' test answer replaced.");
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
		return newTestScenarioQuestion;
	}

	/**
	 * Replaces a child and maintains the order
	 * 
	 * @param oldChild
	 * @param newChild
	 */
	private void replaceTreeObjectChild(TreeObject oldChild, TreeObject newChild) {
		try {
			TreeObject parent = oldChild.getParent();
			Integer childIndex = parent.getIndex(oldChild);
			oldChild.remove();
			parent.addChild(childIndex, newChild);
		} catch (NotValidChildException | DependencyExistException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	public List<String> getScenarioModifications() {
		return scenarioModifications;
	}

	private void checkRemainingFormStructure(TreeObject treeObject, TreeObject testScenarioObjectParent) {
		List<TreeObject> testScenarioObjectsFound = getTestScenarioObjectsWithOriginalReference(
				treeObject.getOriginalReference(), testScenarioObjectParent.getAll(TreeObject.class));
		if (testScenarioObjectsFound.isEmpty()) {
			createTestScenarioObject(treeObject, testScenarioObjectParent);
		} else {
			if (!(treeObject instanceof Question)) {
				for (TreeObject testScenarioObject : testScenarioObjectsFound) {
					for (TreeObject child : treeObject.getChildren()) {
						checkRemainingFormStructure(child, testScenarioObject);
					}
				}
			}
		}
	}

	private List<TreeObject> getTestScenarioObjectsWithOriginalReference(String originalReference,
			List<TreeObject> testScenarioChildren) {
		List<TreeObject> objectsFound = new ArrayList<TreeObject>();
		for (TreeObject child : testScenarioChildren) {
			if (child.getOriginalReference().equals(originalReference)) {
				objectsFound.add(child);
			}
		}
		return objectsFound;
	}
}
