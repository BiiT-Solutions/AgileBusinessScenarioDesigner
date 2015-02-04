package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewRuleTableWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class RuleTableEditor extends LeftTreeTableWebpage{

	private static final String SAVE_BUTTON = "Save";
	private static final String RULE_TABLE_EDITOR_ID = "rule-tables-table";
	private NewRuleTableWindow newRuleTableWindow;
	
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
}
