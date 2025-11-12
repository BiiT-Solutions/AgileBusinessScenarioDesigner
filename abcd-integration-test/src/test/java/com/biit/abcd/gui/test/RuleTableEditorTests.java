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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.NotificationElement;

@Test(groups = "ruleTableEditor")
public class RuleTableEditorTests extends AbcdTester {

	private static final String FORM_1 = "form_1";
	private static final String TABLE_1 = "table_1";
	private static final String VALUE = "0.7";
	private static final String VALUE_1 = "question_3";
	private static final String VALUE_2 = "answer_1";
	private static final String VALUE_3 = "category_1.var3 = 0.7";
	private static final String VALUE_4 = "question_4";
	private static final String VALUE_5 = "answer_4";
	private static final String VALUE_6 = "answer_2";
	private static final String VALUE_NULL = "null";;

	@Test
	public void testAddAndRemoveTable() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);
		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().save();

		getRuleTableEditor().removeTable(0);

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createTwoTablesWithSameName() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);
		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().newRuleTable(TABLE_1);
		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsError(notification);
		notification.close();

		getRuleTableEditor().getNewRuleTableWindow().clickCancel();
		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createATableAddRowWithQuestionAnswerAndAction() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addRow();
		getRuleTableEditor().addCol();

		getRuleTableEditor().getTable().waitForVaadin();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickAction(0);

		getRuleTableEditor().getRuleWindow().expandAnElement(1);
		getRuleTableEditor().getRuleWindow().addVariable(1, VAR_3);

		getRuleTableEditor().getRuleWindow().changeTab(1);
		getRuleTableEditor().getRuleWindow().clickAssign();
		getRuleTableEditor().getRuleWindow().addInputValue(AnswerFormat.NUMBER, VALUE);

		getRuleTableEditor().getRuleWindow().clickAccept();

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createDataInTableAndCheckPersistance() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addRow();
		getRuleTableEditor().addCol();

		getRuleTableEditor().getTable().waitForVaadin();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickAction(0);

		getRuleTableEditor().getRuleWindow().expandAnElement(1);
		getRuleTableEditor().getRuleWindow().addVariable(1, VAR_3);

		getRuleTableEditor().getRuleWindow().changeTab(1);
		getRuleTableEditor().getRuleWindow().clickAssign();
		getRuleTableEditor().getRuleWindow().addInputValue(AnswerFormat.NUMBER, VALUE);

		getRuleTableEditor().getRuleWindow().clickAccept();

		getRuleTableEditor().clickFormDesigner();
		getFormDesigner().clickRuleTableEditor();

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 1), VALUE_2);
		Assert.assertEquals(getRuleTableEditor().getTextInActionTable(0), VALUE_3);

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createEmptyTableWithOneRowAndColEmpty() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addRow();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createDataInTableAndCheckOrder() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickCondition(0, 2);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(5);

		getRuleTableEditor().doubleClickCondition(0, 3);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().addRow();
		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(1);

		getRuleTableEditor().save();

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 1), VALUE_2);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 2), VALUE_4);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 3), VALUE_5);

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 1), VALUE_6);

		getRuleTableEditor().goToFormManager();
		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createDataInTableAndCheckDeleteCols() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickCondition(0, 2);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(5);

		getRuleTableEditor().doubleClickCondition(0, 3);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().addRow();
		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(1);

		getRuleTableEditor().save();

		getRuleTableEditor().removeCol(0);

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 0), VALUE_4);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 1), VALUE_5);

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 0), VALUE_NULL);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 1), VALUE_NULL);

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void tryToPutCategoryAsCondition() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(1);

		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsError(notification);
		notification.close();

		getRuleTableEditor().getAddNewConditionWindow().clickCancel();

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void checkVariableFieldNotEditable() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().selectAVariable(0, VAR_1);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().isTokenDisabled(0);
		getRuleTableEditor().getAddNewConditionExpressionWindow().clickCancel();

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void checkConditionInputFieldNotEditable() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(2);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().isTokenDisabled(0);
		getRuleTableEditor().getAddNewConditionExpressionWindow().clickCancel();

		getRuleTableEditor().save();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void createThreeRowsAndDeleteMiddleOne() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickCondition(0, 2);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(5);

		getRuleTableEditor().doubleClickCondition(0, 3);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().addRow();
		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickCondition(0, 2);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(5);

		getRuleTableEditor().doubleClickCondition(0, 3);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(1);

		getRuleTableEditor().addRow();
		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(1);

		getRuleTableEditor().removeRow(1);

		getRuleTableEditor().save();

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 1), VALUE_2);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 2), VALUE_4);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 3), VALUE_5);

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(1, 1), VALUE_6);

		getRuleTableEditor().goToFormManager();
		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void copyRows() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().newRuleTable(TABLE_1);

		getRuleTableEditor().addCol();
		getRuleTableEditor().addCol();

		getRuleTableEditor().save();

		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().doubleClickCondition(0, 2);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(5);

		getRuleTableEditor().doubleClickCondition(0, 3);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(0);

		getRuleTableEditor().addRow();
		getRuleTableEditor().doubleClickCondition(0, 0);
		getRuleTableEditor().getAddNewConditionWindow().expandElement(1);
		getRuleTableEditor().getAddNewConditionWindow().selectAnElement(4);

		getRuleTableEditor().doubleClickCondition(0, 1);
		getRuleTableEditor().getAddNewConditionExpressionWindow().selectAnswer(1);

		getRuleTableEditor().copyAndPasteRow(0);

		getRuleTableEditor().save();

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 1), VALUE_2);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 2), VALUE_4);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(0, 3), VALUE_5);

		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(2, 0), VALUE_1);
		Assert.assertEquals(getRuleTableEditor().getTextInConditionTable(2, 1), VALUE_6);

		getRuleTableEditor().goToFormManager();
		getFormManager().clickRuleTableEditor();

		getRuleTableEditor().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
}
