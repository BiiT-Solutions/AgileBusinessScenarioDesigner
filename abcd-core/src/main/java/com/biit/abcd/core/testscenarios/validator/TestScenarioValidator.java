package com.biit.abcd.core.testscenarios.validator;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestScenarioValidator {

    private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
    private BaseForm testScenarioAnalyzed;
    private List<TestScenarioValidatorMessage> validatorMessages;

    /**
     * Compares the test scenario structure against the form structure and modifies
     * the objects needed
     *
     * @param form
     * @param testScenario
     */
    public void checkAndModifyTestScenarioStructure(Form form, BaseForm testScenario) {
        if (form != null && testScenario != null) {
            testScenarioAnalyzed = testScenario;
            // First check the changes in the structure already existent
            originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
            if (originalReferenceTreeObjectMap.containsKey(testScenario.getOriginalReference())) {
                try {
                    if (!form.getLabel().equals(testScenario.getLabel())) {
                        String oldName = testScenario.getLabel();
                        testScenario.setLabel(form.getLabel());
                        addValidatorMessages("form.label.changed", oldName, form.getLabel());
                    }
                } catch (FieldTooLongException e) {
                    AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
                }

                List<TreeObject> children = testScenario.getChildren();
                for (TreeObject treeObject : children) {
                    if (treeObject instanceof TestScenarioCategory) {
                        checkTestScenarioCategoryStructure((TestScenarioCategory) treeObject);
                    }
                }
            }
            for (TreeObject treeObject : form.getChildren()) {
                checkRemainingFormStructure(treeObject, testScenario);
            }
        }
    }

    private void checkTestScenarioCategoryStructure(TestScenarioCategory testScenarioCategory) {
        try {
            if (!originalReferenceTreeObjectMap.containsKey(testScenarioCategory.getOriginalReference())) {
                addValidatorMessages("category.deleted", testScenarioCategory.getName());
                testScenarioCategory.remove();
            } else {
                TreeObject category = originalReferenceTreeObjectMap.get(testScenarioCategory.getOriginalReference());
                if (!category.getName().equals(testScenarioCategory.getName())) {
                    String oldName = testScenarioCategory.getName();
                    testScenarioCategory.setName(category.getName());
                    addValidatorMessages("category.name.changed", oldName, category.getName());
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
        } catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException
                 | ElementIsReadOnly e) {
            AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
        }
    }

    private void checkTestScenarioGroupStructure(TestScenarioGroup testScenarioGroup) {
        try {
            // If the group doesn't exists in the original form
            if (!originalReferenceTreeObjectMap.containsKey(testScenarioGroup.getOriginalReference())) {
                addValidatorMessages("group.deleted", testScenarioGroup.getName());
                testScenarioGroup.remove();
            } else {
                TreeObject group = originalReferenceTreeObjectMap.get(testScenarioGroup.getOriginalReference());
                if (group.getPathName().equals(testScenarioGroup.getPathName())) {
                    // If the group exists but has different attributes
                    if ((group instanceof Group)) {
                        if (!testScenarioGroup.isRepeatable() && (((Group) group).isRepeatable())) {
                            testScenarioGroup.setRepeatable(true);
                            addValidatorMessages("group.set.to.repeatable", testScenarioGroup.getName());

                        } else if (testScenarioGroup.isRepeatable() && !((Group) group).isRepeatable()) {
                            keepFirstBornGroup(testScenarioGroup);
                            addValidatorMessages("group.set.to.not.repeatable", testScenarioGroup.getName());
                        }
                    }
                } else {
                    // If the group exists but is in a different position
                    TreeObject whatToMove = testScenarioGroup;
                    TreeObject oldParent = testScenarioGroup.getParent();
                    TreeObject whereToMove = testScenarioAnalyzed.getOriginalReferenceTreeObjectMap()
                            .get(group.getParent().getOriginalReference());
                    TreeObject.move(whatToMove, whereToMove);

                    addValidatorMessages("group.moved", testScenarioGroup.getName(), oldParent.getName(),
                            testScenarioGroup.getParent().getName());

                }
                if (!group.getName().equals(testScenarioGroup.getName())) {
                    String oldName = testScenarioGroup.getName();
                    testScenarioGroup.setName(group.getName());

                    addValidatorMessages("group.name.changed", oldName, testScenarioGroup.getName());
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
                 | DependencyExistException | ChildrenNotFoundException | ElementIsReadOnly e) {
            AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
        }
    }

    private void checkTestScenarioQuestionStructure(TestScenarioQuestion testScenarioQuestion) {
        try {
            if (!originalReferenceTreeObjectMap.containsKey(testScenarioQuestion.getOriginalReference())) {
                addValidatorMessages("question.deleted", testScenarioQuestion.getName());
                testScenarioQuestion.remove();

            } else {
                TreeObject question = originalReferenceTreeObjectMap.get(testScenarioQuestion.getOriginalReference());
                if (!question.getName().equals(testScenarioQuestion.getName())) {
                    String oldName = testScenarioQuestion.getName();
                    testScenarioQuestion.setName(question.getName());
                    addValidatorMessages("question.name.changed", oldName, question.getName());
                }
                checkAnswerType((Question) question, testScenarioQuestion);
            }
        } catch (DependencyExistException | FieldTooLongException | CharacterNotAllowedException
                 | ElementIsReadOnly e) {
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

        } catch (DependencyExistException | ElementIsReadOnly e) {
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
                addValidatorMessages("new.category.created", testScenarioCategory.getName());
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
                addValidatorMessages("new.group.created", testScenarioGroup.getName());
                // Copy children
                for (TreeObject treeObject : formTreeObject.getChildren()) {
                    createTestScenarioObject(treeObject, testScenarioGroup);
                }
            } else if (formTreeObject instanceof Question) {
                TestScenarioQuestion testScenarioQuestion = new TestScenarioQuestion();
                testScenarioQuestion.setOriginalReference(formTreeObject.getOriginalReference());
                testScenarioQuestion.setName(formTreeObject.getName());
                testScenarioTreeObjectParent.addChild(testScenarioQuestion);
                addValidatorMessages("new.question.created", testScenarioQuestion.getName());
            }
            // Any other tree object type not taken into account
        } catch (FieldTooLongException | CharacterNotAllowedException | NotValidChildException | ElementIsReadOnly e) {
            AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
        }
    }

    /**
     * Only validates the structure, not modifies the test scenario.
     *
     * @param form
     * @param testScenario
     * @return true if is valid
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
                    case MULTI_TEXT:
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
        addValidatorMessages("answer.replaced", oldTestAnswer.getClass().getSimpleName(),
                newTestAnswer.getClass().getSimpleName());
    }

    private TestScenarioQuestion replaceTestScenarioQuestion(TestScenarioQuestion testScenarioQuestion) {
        TestScenarioQuestion newTestScenarioQuestion = null;
        try {
            replaceTreeObjectChild(testScenarioQuestion, testScenarioQuestion.copyTestScenarioQuestion());
            addValidatorMessages("question.replaced", testScenarioQuestion.getName());
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
        } catch (NotValidChildException | DependencyExistException | ElementIsReadOnly e) {
            AbcdLogger.errorMessage(TestScenarioValidator.class.getName(), e);
        }
    }

    public List<TestScenarioValidatorMessage> getValidatorMessages() {
        return validatorMessages;
    }

    public void addValidatorMessages(String identifier, String... parameters) {
        if (validatorMessages == null) {
            validatorMessages = new ArrayList<TestScenarioValidatorMessage>();
        }
        validatorMessages.add(new TestScenarioValidatorMessage(identifier, parameters));
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
