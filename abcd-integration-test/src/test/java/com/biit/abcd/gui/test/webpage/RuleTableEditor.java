package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewRuleTableWindow;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class RuleTableEditor extends LeftTreeTableWebpage {

	private static final String SAVE_BUTTON = "Save";
	private static final String RULE_TABLE_EDITOR_ID = "rule-tables-table";
	private static final String REMOVE_TABLE_CAPTION = "Remove";
	private static final String REMOVE_CAPTION = "Remove";
	private static final String ADD_ROW_CAPTION = "Add";
	private static final String ADD_COL_CAPTION = "Add";
	private static final String ACTION_TABLE_ID = "freeze-pane";
	private static final String QUESTION_ANSWER_TABLE_ID = "main-table";
	private final NewRuleTableWindow newRuleTableWindow;

	public RuleTableEditor() {
		super();
		newRuleTableWindow = new NewRuleTableWindow();
		addWindow(newRuleTableWindow);
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

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}

	public void removeTable(int row) {
		selectRow(row);
		getRemoveTableButton().click();
		getRemoveTableButton().waitForVaadin();
		getProceed().clickAccept();
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
	
	public NewRuleTableWindow getNewRuleTableWindow() {
		return newRuleTableWindow;
	}

	public void addRow() {
		getAddRowButton().click();
		getAddRowButton().waitForVaadin();
	}

	private ButtonElement getAddRowButton() {
		return $(ButtonElement.class).caption(ADD_ROW_CAPTION).get(1);
	}
	
	public void addCol(){
		getAddColButton().click();
		getAddColButton().waitForVaadin();
	}

	private TestBenchElement getAddColButton() {
		return $(ButtonElement.class).caption(ADD_COL_CAPTION).first();
	}
	
	private TableElement getQuestionAnswerTable(){
		return $(TableElement.class).id(QUESTION_ANSWER_TABLE_ID);
	}
	
	private TableElement getActionTable(){
		return $(TableElement.class).id(ACTION_TABLE_ID);
	}
}
