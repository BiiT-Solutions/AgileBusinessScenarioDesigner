package com.biit.abcd.gui.test.webpage;

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

import org.openqa.selenium.By;

import com.biit.abcd.gui.test.window.AddNewConditionExpressionWindow;
import com.biit.abcd.gui.test.window.AddNewConditionWindow;
import com.biit.abcd.gui.test.window.NewRuleTableWindow;
import com.biit.abcd.gui.test.window.RuleWindow;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TableElement;

public class RuleTableEditor extends LeftTreeTableWebpage {

	private static final String RULE_TABLE_EDITOR_ID = "rule-tables-table";
	private static final String REMOVE_TABLE_CAPTION = "Remove";
	private static final String REMOVE_CAPTION = "Remove";
	private static final String ADD_ROW_CAPTION = "Add";
	private static final String ADD_COL_CAPTION = "Add";
	private static final String ACTION_TABLE_ID = "freeze-pane";
	private static final String QUESTION_ANSWER_TABLE_ID = "main-table";
	private static final String COPY_CAPTION = "Copy";
	private static final String PASTE_CAPTION = "Paste";
	private final NewRuleTableWindow newRuleTableWindow;
	private final AddNewConditionWindow addNewConditionWindow;
	private final AddNewConditionExpressionWindow addNewConditionExpressionWindow;
	private final RuleWindow ruleWindow; 

	public RuleTableEditor() {
		super();
		newRuleTableWindow = new NewRuleTableWindow();
		addNewConditionWindow = new AddNewConditionWindow();
		addNewConditionExpressionWindow = new AddNewConditionExpressionWindow();
		ruleWindow = new RuleWindow();
		addWindow(newRuleTableWindow);
		addWindow(addNewConditionWindow);
		addWindow(addNewConditionExpressionWindow);
		addWindow(ruleWindow);
	}

	@Override
	public TableElement getTable() {
		TableElement query = $(TableElement.class).id(getTableId());
		return query;
	}

	@Override
	protected String getTableId() {
		return RULE_TABLE_EDITOR_ID;
	}

	public void newRuleTable(String name) {
		getNewButton().click();
		newRuleTableWindow.setNameAndAccept(name);
	}

	public void removeTable(int row) {
		selectRow(row);
		getRemoveTableButton().click();
		getRemoveTableButton().waitForVaadin();
		getProceedWindow().clickAccept();
	}

	private TestBenchElement getRemoveTableButton() {
		return $(ButtonElement.class).caption(REMOVE_TABLE_CAPTION).first();
	}

	private TestBenchElement getRemoveColButton() {
		return $(ButtonElement.class).caption(REMOVE_CAPTION).get(1);
	}

	private TestBenchElement getRemoveRowButton() {
		return $(ButtonElement.class).caption(REMOVE_CAPTION).get(2);
	}
	
	private TestBenchElement getCopyButton(){
		return getButtonByCaption(COPY_CAPTION);
	}
	
	private TestBenchElement getPasteButton(){
		return getButtonByCaption(PASTE_CAPTION);
	}

	public NewRuleTableWindow getNewRuleTableWindow() {
		return newRuleTableWindow;
	}

	public void addRow() {
		getAddRowButton().click();
		getAddRowButton().waitForVaadin();
	}
	
	public void removeCol(int col){
		clickCondition(0,col);
		getRemoveColButton().click();
		getRemoveColButton().waitForVaadin();
	}

	private ButtonElement getAddRowButton() {
		return $(ButtonElement.class).caption(ADD_ROW_CAPTION).get(1);
	}

	public void addCol() {
		getAddColButton().click();
		getAddColButton().waitForVaadin();
	}

	private TestBenchElement getAddColButton() {
		return $(ButtonElement.class).caption(ADD_COL_CAPTION).first();
	}

	public TableElement getQuestionAnswerTable() {
		return $(TableElement.class).id(QUESTION_ANSWER_TABLE_ID);
	}

	public TableElement getActionTable() {
		return $(TableElement.class).id(ACTION_TABLE_ID);
	}

	public void clickCondition(int row, int column) {
		LabelElement label = ((TestBenchElement) getQuestionAnswerTable().getRow(row)
				.findElements(By.className("v-label")).get(column)).wrap(LabelElement.class);
		label.click();
		label.waitForVaadin();
	}
	
	public void doubleClickCondition(int row, int column) {
		LabelElement label = ((TestBenchElement) getQuestionAnswerTable().getRow(row)
				.findElements(By.className("v-label")).get(column)).wrap(LabelElement.class);
		label.doubleClick();
		label.waitForVaadin();
	}

	public void doubleClickAction(int row) {
		getActionTable().getCell(row, 0).click();
		getActionTable().getCell(row, 0).doubleClick();
	}
	
	public AddNewConditionWindow getAddNewConditionWindow() {
		return addNewConditionWindow;
	}
	
	public AddNewConditionExpressionWindow getAddNewConditionExpressionWindow() {
		return addNewConditionExpressionWindow;
	}

	public RuleWindow getRuleWindow() {
		return ruleWindow;
	}

	public String getTextInConditionTable(int row, int col) {
		LabelElement label = ((TestBenchElement) getQuestionAnswerTable().getRow(row)
				.findElements(By.className("v-label")).get(col)).wrap(LabelElement.class);
		return label.getText();
	}
	
	public String getTextInActionTable(int row) {
		LabelElement label = ((TestBenchElement) getActionTable().getRow(row)
				.findElements(By.className("v-label")).get(0)).wrap(LabelElement.class);
		return label.getText();
	}

	public void removeRow(int row) {
		clickCondition(row, 0);
		getRemoveRowButton().click();
		getRemoveRowButton().waitForVaadin();
	}

	public void copyAndPasteRow(int row) {
		clickCondition(row, 0);
		getCopyButton().click();
		getCopyButton().waitForVaadin();
		getPasteButton().click();
		getPasteButton().waitForVaadin();
	}
}
