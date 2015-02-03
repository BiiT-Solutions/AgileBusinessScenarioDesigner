package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewRuleExpressionWindow;
import com.vaadin.testbench.elements.TableElement;

public class RuleExpression extends LeftTreeTableWebpage{
	
	private static final String EXPRESSION_TABLE_CAPTION = "expression-table";
	private NewRuleExpressionWindow newRuleExpressionWindow;

	public RuleExpression() {
		super();
		newRuleExpressionWindow = new NewRuleExpressionWindow();
		addWindow(newRuleExpressionWindow);
	}
		
	public void newRuleExpression(String name){
		getNewButton().click();
		newRuleExpressionWindow.setNameAndAccept(name);
	}

	@Override
	protected String getTableId() {
		return EXPRESSION_TABLE_CAPTION;
	}

	@Override
	public TableElement getTable() {
		TableElement query = $(TableElement.class).id(getTableId());
		return query;
	}
	
}
