package com.biit.abcd.core.drools.facts.inputform.importer;

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

	private static HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private static TestScenario testScenarioAnalyzed;

	/**
	 * Compares the test scenario structure against the form structure and
	 * modifies the objects needed
	 * 
	 * @param form
	 * @param testScenario
	 */
	public static void checkAndModifyTestScenarioStructure(Form form, TestScenario testScenario) {
		if (form != null && testScenario != null) {

			printTestScenarioOrder(testScenario.getTestScenarioForm(), "");

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
			} else {
				// TODO send form not found exception
			}

			// Second add the new structure needed
			originalReferenceTreeObjectMap = testScenario.getTestScenarioForm().getOriginalReferenceTreeObjectMap();
			for (TreeObject treeObject : form.getChildren()) {
				checkRemainingFormStructure(treeObject, testScenario.getTestScenarioForm());
			}

			printTestScenarioOrder(testScenario.getTestScenarioForm(), "");
		}
	}

	private static void printTestScenarioOrder(TreeObject treeObject, String tabs) {
		System.out.println(tabs + treeObject.getName());
		if (treeObject.getChildren() != null) {
			for (TreeObject child : treeObject.getChildren()) {
				printTestScenarioOrder(child, tabs + "\t");
			}
		}
	}

	private static void checkTestScenarioCategoryStructure(TestScenarioCategory testScenarioCategory) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioCategory.getOriginalReference())) {
				testScenarioCategory.remove();
				System.out.println("CATEGORY DELETED");
			} else {
				TreeObject category = originalReferenceTreeObjectMap.get(testScenarioCategory.getOriginalReference());
				if (!category.getName().equals(testScenarioCategory.getName())) {
					String oldName = testScenarioCategory.getName();
					testScenarioCategory.setName(category.getName());
					System.out.println("CATEGORY NAME CHANGED FROM: " + oldName + " TO:" + category.getName());
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

	private static void checkTestScenarioGroupStructure(TestScenarioGroup testScenarioGroup) {
		try {
			// If the group doesn't exists in the original form
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioGroup.getOriginalReference())) {
				testScenarioGroup.remove();
				System.out.println("GROUP DELETED");
			} else {
				TreeObject group = originalReferenceTreeObjectMap.get(testScenarioGroup.getOriginalReference());
				if (group.getPathName().equals(testScenarioGroup.getPathName())) {
					// If the group exists but has different attributes
					if ((group instanceof Group)) {
						if (!testScenarioGroup.isRepeatable() && (((Group) group).isRepeatable())) {
							TestScenarioGroup newTestScenarioGroup = testScenarioGroup.copyTestScenarioGroup();
							newTestScenarioGroup.setRepeatable(true);
							replaceTestScenarioGroup(testScenarioGroup, newTestScenarioGroup);
							System.out.println("GROUP REPLACED - Test scenario group not repeatable, Form group repeatable");

						} else if (testScenarioGroup.isRepeatable() && !((Group) group).isRepeatable()) {
							TestScenarioGroup newTestScenarioGroup = testScenarioGroup.copyTestScenarioGroup();
							newTestScenarioGroup.setRepeatable(false);
							replaceTestScenarioGroup(testScenarioGroup, newTestScenarioGroup);
							System.out.println("GROUP REPLACED - Test scenario group repeatable, Form group not repeatable");
						}
					}
				} else {
					// If the group exists but is in a different position
					TreeObject whatToMove = testScenarioGroup;
					TreeObject whereToMove = testScenarioAnalyzed.getTestScenarioForm()
							.getOriginalReferenceTreeObjectMap().get(group.getParent().getOriginalReference());
					TreeObject.move(whatToMove, whereToMove);
					System.out.println("GROUP MOVED");
				}
				if (!group.getName().equals(testScenarioGroup.getName())) {
					String oldName = testScenarioGroup.getName();
					testScenarioGroup.setName(group.getName());
					System.out.println("CATEGORY NAME CHANGED FROM: " + oldName + " TO:" + testScenarioGroup.getName());
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

	private static void checkTestScenarioQuestionStructure(TestScenarioQuestion testScenarioQuestion) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioQuestion.getOriginalReference())) {
				testScenarioQuestion.remove();
				System.out.println("QUESTION DELETED");

			} else {
				TreeObject treeObject = originalReferenceTreeObjectMap.get(testScenarioQuestion.getOriginalReference());
				if (!treeObject.getName().equals(testScenarioQuestion.getName())) {
					String oldName = testScenarioQuestion.getName();
					testScenarioQuestion.setName(treeObject.getName());
					System.out.println("QUESTION NAME CHANGED FROM: " + oldName + " TO:" + treeObject.getName());
				}
				checkAnswerType((Question) treeObject, testScenarioQuestion);
			}
		} catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private static void replaceTestScenarioGroup(TestScenarioGroup oldValue, TestScenarioGroup newValue) {
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

	private static void checkRemainingFormStructure(TreeObject treeObject, TreeObject testScenarioObjectParent) {
		if (!originalReferenceTreeObjectMap.containsKey(treeObject.getOriginalReference())) {
			createTestScenarioObject(treeObject, testScenarioObjectParent);
		} else {
			for (TreeObject child : treeObject.getChildren()) {
				checkRemainingFormStructure(child,
						originalReferenceTreeObjectMap.get(treeObject.getOriginalReference()));
			}
		}
	}

	private static void createTestScenarioObject(TreeObject formTreeObject, TreeObject testScenarioTreeObjectParent) {
		if (!originalReferenceTreeObjectMap.containsKey(formTreeObject.getOriginalReference())) {
			try {
				if (formTreeObject instanceof Category) {
					TestScenarioCategory testScenarioCategory = new TestScenarioCategory();
					testScenarioCategory.setOriginalReference(formTreeObject.getOriginalReference());
					testScenarioCategory.setName(formTreeObject.getName());
					testScenarioTreeObjectParent.addChild(testScenarioCategory);
					originalReferenceTreeObjectMap.put(testScenarioCategory.getOriginalReference(),
							testScenarioCategory);
					// Copy children
					for (TreeObject treeObject : formTreeObject.getChildren()) {
						createTestScenarioObject(treeObject, testScenarioCategory);
					}
				} else if (formTreeObject instanceof Group) {
					TestScenarioGroup testScenarioGroup = new TestScenarioGroup();
					testScenarioGroup.setOriginalReference(formTreeObject.getOriginalReference());
					testScenarioGroup.setName(formTreeObject.getName());
					testScenarioTreeObjectParent.addChild(testScenarioGroup);
					originalReferenceTreeObjectMap.put(testScenarioGroup.getOriginalReference(), testScenarioGroup);
					// Copy children
					for (TreeObject treeObject : formTreeObject.getChildren()) {
						createTestScenarioObject(treeObject, testScenarioGroup);
					}
				} else if (formTreeObject instanceof Question) {
					TestScenarioQuestion testScenarioQuestion = new TestScenarioQuestion();
					testScenarioQuestion.setOriginalReference(formTreeObject.getOriginalReference());
					testScenarioQuestion.setName(formTreeObject.getName());
					testScenarioTreeObjectParent.addChild(testScenarioQuestion);
					originalReferenceTreeObjectMap.put(testScenarioQuestion.getOriginalReference(),
							testScenarioQuestion);
				}
				// Any other tree object type not taken into account
			} catch (FieldTooLongException | CharacterNotAllowedException | NotValidChildException e) {
				AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
			}
		}
	}

	/**
	 * Only validates the structure, not modifies the test scenario.
	 * 
	 * @param form
	 * @param testScenario
	 * @return
	 */
	public static boolean validateToLaunch(Form form, TestScenario testScenario) {
		originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
		return validateToLaunch(testScenario.getTestScenarioForm());
	}

	private static boolean validateToLaunch(TreeObject testScenarioObject) {
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
	private static void checkAnswerType(Question question, TestScenarioQuestion testScenarioQuestion) {
		TestAnswer oldTestAnswer = testScenarioQuestion.getTestAnswer();
		TestAnswer testAnswer = testScenarioQuestion.getTestAnswer();
		switch (question.getAnswerType()) {
		case RADIO:
			if (!(testAnswer instanceof TestAnswerRadioButton)) {
				TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
				if (newTestScenarioQuestion != null) {
					newTestScenarioQuestion.setTestAnswer(new TestAnswerRadioButton());
					System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
							+ " TO: TestAnswerRadioButton");
				}
			}
			break;
		case MULTI_CHECKBOX:
			if (!(testAnswer instanceof TestAnswerMultiCheckBox)) {
				TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
				if (newTestScenarioQuestion != null) {
					newTestScenarioQuestion.setTestAnswer(new TestAnswerMultiCheckBox());
					System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
							+ " TO: TestAnswerMultiCheckBox");
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
						System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
								+ " TO: TestAnswerInputText");
					}
				}
				break;
			case POSTAL_CODE:
				if (!(testAnswer instanceof TestAnswerInputPostalCode)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputPostalCode());
						System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
								+ " TO: TestAnswerInputPostalCode");
					}
				}
				break;
			case NUMBER:
				if (!(testAnswer instanceof TestAnswerInputNumber)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputNumber());
						System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
								+ " TO: TestAnswerInputNumber");
					}
				}
				break;
			case DATE:
				if (!(testAnswer instanceof TestAnswerInputDate)) {
					TestScenarioQuestion newTestScenarioQuestion = replaceTestScenarioQuestion(testScenarioQuestion);
					if (newTestScenarioQuestion != null) {
						newTestScenarioQuestion.setTestAnswer(new TestAnswerInputDate());
						System.out.println("QUESTION TEST ANSWER REPLACED: " + oldTestAnswer.getClass().getName()
								+ " TO: TestAnswerInputDate");
					}
				}
				break;
			}
		}
	}

	private static TestScenarioQuestion replaceTestScenarioQuestion(TestScenarioQuestion testScenarioQuestion) {
		TestScenarioQuestion newTestScenarioQuestion = null;
		try {
			replaceTreeObjectChild(testScenarioQuestion, testScenarioQuestion.copyTestScenarioQuestion());
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
	private static void replaceTreeObjectChild(TreeObject oldChild, TreeObject newChild) {
		try {
			TreeObject parent = oldChild.getParent();
			Integer childIndex = parent.getIndex(oldChild);
			oldChild.remove();
			parent.addChild(childIndex, newChild);
		} catch (NotValidChildException | DependencyExistException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}
}
