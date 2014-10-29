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
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class TestScenarioValidator {

	private static HashMap<String, TreeObject> originalReferenceTreeObjectMap;

	/**
	 * Compares the test scenario structure against the form structure and
	 * modifies the objects needed
	 * 
	 * @param form
	 * @param testScenario
	 */
	public static void checkTestScenarioStructure(Form form, TestScenario testScenario) {
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
	}

	private static void checkTestScenarioCategoryStructure(TestScenarioCategory testScenarioCategory) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioCategory.getOriginalReference())) {
				testScenarioCategory.remove();
			} else {
				TreeObject category = originalReferenceTreeObjectMap.get(testScenarioCategory.getOriginalReference());
				if (!category.getName().equals(testScenarioCategory.getName())) {
					testScenarioCategory.setName(category.getName());
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
			} else {
				// If the group exists but has different attributes
				TreeObject group = originalReferenceTreeObjectMap.get(testScenarioGroup.getOriginalReference());
				if ((group instanceof Group)) {
					if (!testScenarioGroup.isRepeatable() && (((Group) group).isRepeatable())) {
						TestScenarioGroup newTestScenarioGroup = testScenarioGroup.copyTestScenarioGroup();
						newTestScenarioGroup.setRepeatable(true);
						replaceTestScenarioGroup(testScenarioGroup, newTestScenarioGroup);

					} else if (testScenarioGroup.isRepeatable() && !((Group) group).isRepeatable()) {
						TestScenarioGroup newTestScenarioGroup = testScenarioGroup.copyTestScenarioGroup();
						newTestScenarioGroup.setRepeatable(false);
						replaceTestScenarioGroup(testScenarioGroup, newTestScenarioGroup);
					}
				}
				if (!group.getName().equals(testScenarioGroup.getName())) {
					testScenarioGroup.setName(group.getName());
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
				| DependencyExistException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private static void checkTestScenarioQuestionStructure(TestScenarioQuestion testScenarioQuestion) {
		try {
			if (!originalReferenceTreeObjectMap.containsKey(testScenarioQuestion.getOriginalReference())) {
				testScenarioQuestion.remove();

			} else {
				TreeObject treeObject = originalReferenceTreeObjectMap.get(testScenarioQuestion.getOriginalReference());
				if (!treeObject.getName().equals(testScenarioQuestion.getName())) {
					testScenarioQuestion.setName(treeObject.getName());
				}
			}
		} catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
		}
	}

	private static void replaceTestScenarioGroup(TestScenarioGroup oldValue, TestScenarioGroup newValue) {
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
			for (TreeObject treeObject : childToRemove) {
				treeObject.remove();
			}
			// After removing the children we add the new child
			parent.addChild(newValue);

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
		TestScenarioForm testScenarioForm = testScenario.getTestScenarioForm();
		if (originalReferenceTreeObjectMap.containsKey(testScenarioForm.getOriginalReference())) {
			for (TreeObject testScenarioObject : testScenarioForm.getChildren()) {
				return validateToLaunch(testScenarioObject);
			}
		}
		return false;
	}

	private static boolean validateToLaunch(TreeObject testScenarioObject) {
		if (originalReferenceTreeObjectMap.containsKey(testScenarioObject.getOriginalReference())) {
			if (testScenarioObject instanceof TestScenarioQuestion) {
				return true;
			} else {
				for (TreeObject treeObject : testScenarioObject.getChildren()) {
					return validateToLaunch(treeObject);
				}
			}
		}
		return false;
	}
}
