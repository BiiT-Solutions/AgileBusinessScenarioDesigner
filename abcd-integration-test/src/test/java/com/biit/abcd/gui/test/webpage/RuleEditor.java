package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewRuleWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class RuleEditor extends LeftTreeTableWebpage{

	private static final String SAVE_BUTTON = "Save";
	private static final String ID = "rule-table";
	
	private NewRuleWindow newRuleWindow;
	
	public RuleEditor() {
		super();
		newRuleWindow = new NewRuleWindow();
		addWindow(newRuleWindow);
	}

	@Override
	public TableElement getTable() {
		TableElement query = $(TableElement.class).id(getTableId());
		return query;
	}

	@Override
	protected String getTableId() {
		return ID;
	}

	public void newRule(String name) {
		getNewButton().click();
		newRuleWindow.setNameAndAccept(name);
	}

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}
}
