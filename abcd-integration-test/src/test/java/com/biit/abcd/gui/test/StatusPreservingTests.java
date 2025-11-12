package com.biit.abcd.gui.test;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
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

import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerType;

@Test(groups = "statusPreserving")
public class StatusPreservingTests extends AbcdTester {

	private static final String TEST_FORM_1 = "test_1";
	private static final String TEST_FORM_1_CHANGED_NAME = "test_1_changed";
	private static final String TEST_FORM_2 = "test_2";
	private static final int FORM_1_ROW = 1;
	private static final int FORM_2_ROW = 3;
	private static final String DIAGRAM_1 = "diag_1";
	private static final String DIAGRAM_2 = "diag_2";
	private static final int DIAGRAM_1_ROW = 0;
	private static final int DIAGRAM_2_ROW = 1;
	private static final String RULE_1 = "rule_1";
	private static final String RULE_2 = "rule_2";
	private static final int RULE_1_ROW = 0;
	private static final int RULE_2_ROW = 1;
	private static final String TABLE_1 = "table_1";
	private static final String TABLE_2 = "table_2";
	private static final int TABLE_1_ROW = 0;
	private static final int TABLE_2_ROW = 1;
	private static final String CATEGORY_1 = "category1";
	private static final String CATEGORY_1_MODIFIED = "category1m";
	private static final String GROUP_1 = "group1";
	private static final String GROUP_1_MODIFIED = "group1m";
	private static final String QUESTION_1 = "question1";
	private static final String QUESTION_1_MODIFIED = "question1m";
	private static final String ANSWER_1 = "answer1";
	private static final String ANSWER_1_MODIFIED = "answer1m";
	private static final String SUBANSWER_1 = "subanswer1";
	private static final String SUBANSWER_1_MODIFIED = "subanswer1m";
	private static final String VARIABLE_1 = "variable1";
	private static final String VARIABLE_1_VALUE = "0.0";
	private static final int VARIABLE_1_ROW = 0;
	
	public enum Scope {

		FORM("Form"),

		CATEGORY("Category"),

		GROUP("Group"),

		QUESTION("Question"),;

		private String value;

		Scope(String caption) {
			this.value = caption;
		}

		public String getValue() {
			return value;
		}
	}

	private void formDesignerCheckSaveWarning() {
		// Go to Form Designer without saving check warning is visible
		getFormDesigner().goToFormManager();
		Assert.assertTrue(getFormDesigner().getWarningUnsavedData().isVisible());
		getFormDesigner().getWarningUnsavedData().clickCancel();

		// Go to Form Designer after saving check warning is not visible
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		Assert.assertFalse(getFormDesigner().getWarningUnsavedData().isVisible());
		getFormManager().clickFormDesigner();
	}

	private void expandForm() {
		getFormDesigner().getDesignTable().getRow(1).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(2).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(3).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(4).toggleExpanded();
	}

	private void formVariablesCheckSaveWarningAndSave() {
		// Go to Form Designer without saving check warning is visible
		getFormVariables().goToFormManager();
		Assert.assertTrue(getFormVariables().getWarningUnsavedData().isVisible());
		getFormVariables().getWarningUnsavedData().clickCancel();

		// Go to Form Designer after saving check warning is not visible
		getFormVariables().save();
		getFormVariables().goToFormManager();
		Assert.assertFalse(getFormVariables().getWarningUnsavedData().isVisible());
		getFormManager().clickFormVariables();
	}

	private void ruleExpressionCheckSaveWarning() {
		// Go to Form Designer without saving check warning is visible
		getRuleExpression().goToFormManager();
		Assert.assertTrue(getRuleExpression().getWarningUnsavedData().isVisible());
		getRuleExpression().getWarningUnsavedData().clickCancel();
		// Go to Form Designer after saving check warning is not visible
		getRuleExpression().save();
		getRuleExpression().goToFormManager();
		Assert.assertFalse(getRuleExpression().getWarningUnsavedData().isVisible());
		getFormManager().clickRuleExpressionEditor();
	}

	private void diagramDesignerCheckSaveWarning() {
		// Go to Form Designer without saving check warning is visible
		getDiagramDesigner().goToFormManager();
		Assert.assertTrue(getDiagramDesigner().getWarningUnsavedData().isVisible());
		getDiagramDesigner().getWarningUnsavedData().clickCancel();

		// Go to Form Designer after saving check warning is not visible
		getDiagramDesigner().save();
		getDiagramDesigner().goToFormManager();
		Assert.assertFalse(getDiagramDesigner().getWarningUnsavedData().isVisible());
		getFormManager().clickDiagramDesigner();

	}

	@Test
	public void formInUseIsSelectedWhenReturning() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().createNewForm(TEST_FORM_2);

		getFormManager().clickFormDesigner();
		sleep(250);
		clickFormManager();
		sleep(250);
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		getFormManager().selectForm(FORM_1_ROW);
		getFormManager().clickFormDesigner();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_1_ROW));

		getFormManager().selectForm(FORM_2_ROW);
		getFormManager().clickFormVariables();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));

		getFormManager().clickDiagramDesigner();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));

		getFormManager().clickRuleExpressionEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));

		getFormManager().clickRuleEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));

		getFormManager().clickRuleTableEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));

		getFormManager().logOut();

		deleteForm(FORM_2_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void elementsSelectedAreMaintainedCorrectly() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickDiagramDesigner();

		// Add two diagrams
		getDiagramDesigner().newDiagram(DIAGRAM_1);
		getDiagramDesigner().newDiagram(DIAGRAM_2);
		getDiagramDesigner().selectRow(DIAGRAM_1_ROW);

		getDiagramDesigner().clickFormDesigner();
		sleep(250);
		getFormDesigner().clickDiagramDesigner();
		sleep(250);
		getDiagramDesigner().isRowSelected(DIAGRAM_1_ROW);
		getDiagramDesigner().selectRow(DIAGRAM_2_ROW);
		getDiagramDesigner().clickFormDesigner();
		sleep(250);
		getFormDesigner().clickDiagramDesigner();
		sleep(250);
		getDiagramDesigner().isRowSelected(DIAGRAM_2_ROW);

		// Rule expression
		getDiagramDesigner().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().newRuleExpression(RULE_2);
		getRuleExpression().selectRow(RULE_1_ROW);

		getRuleExpression().clickFormDesigner();
		getFormDesigner().clickRuleExpressionEditor();
		getRuleExpression().isRowSelected(RULE_1_ROW);
		getRuleExpression().selectRow(RULE_2_ROW);
		getRuleExpression().clickFormDesigner();
		getFormDesigner().clickRuleExpressionEditor();
		getRuleExpression().isRowSelected(RULE_2_ROW);

		// Rule editor
		getFormDesigner().clickRuleEditor();
		getRuleEditor().newRule(RULE_1);
		getRuleEditor().newRule(RULE_2);
		getRuleEditor().selectRow(RULE_1_ROW);

		getRuleEditor().clickFormDesigner();
		getFormDesigner().clickRuleEditor();
		getRuleEditor().isRowSelected(RULE_1_ROW);
		getRuleEditor().selectRow(RULE_2_ROW);
		getRuleEditor().clickFormDesigner();
		getFormDesigner().clickRuleEditor();
		getRuleEditor().isRowSelected(RULE_2_ROW);

		// Rule Table editor
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().newRuleTable(TABLE_1);
		getRuleTableEditor().newRuleTable(TABLE_2);
		getRuleTableEditor().selectRow(TABLE_1_ROW);

		getRuleTableEditor().clickFormDesigner();
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().isRowSelected(TABLE_1_ROW);
		getRuleTableEditor().selectRow(TABLE_2_ROW);
		getRuleTableEditor().clickFormDesigner();
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().isRowSelected(TABLE_2_ROW);

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void savePromptWhenChangedFormName() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickFormDesigner();
		sleep(250);
		getFormDesigner().setTechnicalName(TEST_FORM_1_CHANGED_NAME);
		getFormDesigner().clickInTableRow(0);

		getFormDesigner().goToFormManager();

		Assert.assertTrue(getFormDesigner().getWarningUnsavedData().isVisible());
		getFormDesigner().getWarningUnsavedData().clickCancel();
		getFormDesigner().save();
		getFormDesigner().goToFormManager();

		Assert.assertFalse(getFormDesigner().getWarningUnsavedData().isVisible());

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void savePromptWhenNewFormElementsOrEditing() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickFormDesigner();

		sleep(250);
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_1);

		formDesignerCheckSaveWarning();

		getFormDesigner().createGroup(1, GROUP_1);

		formDesignerCheckSaveWarning();

		getFormDesigner().getDesignTable().getRow(1).toggleExpanded();
		getFormDesigner().createQuestion(2, QUESTION_1, AnswerType.MULTI_CHECKBOX, null);

		formDesignerCheckSaveWarning();

		getFormDesigner().getDesignTable().getRow(1).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(2).toggleExpanded();
		getFormDesigner().createAnswer(3, ANSWER_1);

		formDesignerCheckSaveWarning();

		getFormDesigner().getDesignTable().getRow(1).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(2).toggleExpanded();
		getFormDesigner().getDesignTable().getRow(3).toggleExpanded();
		getFormDesigner().createSubanswer(4, SUBANSWER_1);

		formDesignerCheckSaveWarning();

		expandForm();
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().setTechnicalName(CATEGORY_1_MODIFIED);
		formDesignerCheckSaveWarning();

		expandForm();
		getFormDesigner().clickInTableRow(2);
		getFormDesigner().setTechnicalName(GROUP_1_MODIFIED);
		formDesignerCheckSaveWarning();

		expandForm();
		getFormDesigner().clickInTableRow(3);
		getFormDesigner().setTechnicalName(QUESTION_1_MODIFIED);
		formDesignerCheckSaveWarning();

		expandForm();
		getFormDesigner().clickInTableRow(4);
		getFormDesigner().setTechnicalName(ANSWER_1_MODIFIED);
		formDesignerCheckSaveWarning();

		expandForm();
		getFormDesigner().clickInTableRow(5);
		getFormDesigner().setTechnicalName(SUBANSWER_1_MODIFIED);
		formDesignerCheckSaveWarning();

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();

	}

	@Test
	public void savePromptWhenNewVariable() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickFormVariables();

		getFormVariables().clickAddVariable();
		getFormVariables().getTextField(0, 0).setValue(VARIABLE_1);
		formVariablesCheckSaveWarningAndSave();

		getFormVariables().removeVariable(VARIABLE_1_ROW);
		formVariablesCheckSaveWarningAndSave();

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void savePromptWhenVariableModified() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickFormVariables();

		getFormVariables().clickAddVariable();
		getFormVariables().getTextField(0, 0).setValue(VARIABLE_1);
		formVariablesCheckSaveWarningAndSave();

		getFormVariables().getComboBoxElement(0, 1).selectByText(AnswerFormat.NUMBER.getValue());
		formVariablesCheckSaveWarningAndSave();

		getFormVariables().getComboBoxElement(0, 2).selectByText(Scope.CATEGORY.getValue());
		formVariablesCheckSaveWarningAndSave();

		getFormVariables().getTextField(0, 3).setValue(VARIABLE_1_VALUE);
		formVariablesCheckSaveWarningAndSave();

		// Remove form
		getFormVariables().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void savePromptWhenNewAndRemoveDiagram() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickDiagramDesigner();

		getDiagramDesigner().newDiagram(DIAGRAM_1);
		diagramDesignerCheckSaveWarning();

		// It's being deselected
		getDiagramDesigner().selectRow(DIAGRAM_1_ROW);
		getDiagramDesigner().removeDiagram(DIAGRAM_1_ROW);
		diagramDesignerCheckSaveWarning();

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void savePromptWhenNewAndRemoveRuleExpression() {
		deleteAllForms();
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickRuleExpressionEditor();

		getRuleExpression().newRuleExpression(RULE_1);
		ruleExpressionCheckSaveWarning();

		getRuleExpression().removeRule(RULE_1_ROW);
		diagramDesignerCheckSaveWarning();

		// Remove form
		getRuleTableEditor().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}
}
