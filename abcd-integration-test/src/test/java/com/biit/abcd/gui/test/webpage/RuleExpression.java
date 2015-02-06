package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewRuleExpressionWindow;
import com.biit.abcd.gui.test.window.SelectAVariable;
import com.biit.abcd.gui.test.window.SelectAnElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.PanelElement;
import com.vaadin.testbench.elements.TabSheetElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class RuleExpression extends LeftTreeTableWebpage{
	
	private static final String EXPRESSION_TABLE_CAPTION = "expression-table";
	private static final String ADD_ELEMENT_BUTTON = "Add Element";
	private static final String ADD_VARIABLE_BUTTON = "Add Variable";
	private static final String VARIABLES_TABLE = "Select a Variable:";
	private static final String GENERIC_ELEMENT_TABLE = "Select a Generic Element:";
	private static final String ADD_GENERIC_ELEMENT_BUTTON = "Add Generic Element";
	private final NewRuleExpressionWindow newRuleExpressionWindow;
	private final SelectAnElement selectAnElement;
	private final SelectAVariable selectAVariable;

	public RuleExpression() {
		super();
		newRuleExpressionWindow = new NewRuleExpressionWindow();
		selectAnElement = new SelectAnElement();
		selectAVariable = new SelectAVariable();
		addWindow(newRuleExpressionWindow);
		addWindow(selectAnElement);
		addWindow(selectAVariable);
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

	public void removeRule(int row) {
		selectRow(row);
		getRemoveButton().click();
		getProceed().clickAccept();
	}
	
	public NewRuleExpressionWindow getNewRuleExpressionWindow() {
		return newRuleExpressionWindow;
	}

	public TreeTableElement getSelectElementTable() {
		 return $(TreeTableElement.class).caption("Select an Element:").first();
	}
	
	public void selectElement(int row){
		getSelectElementTable().getCell(row, 0).click();
		getSelectElementTable().waitForVaadin();
	}

	public void toggleSelectElementRow(int row) {
		getSelectElementTable().getRow(row).toggleExpanded();
	}
	
	public ButtonElement getAddElementButton(){
		return getButtonByCaption(ADD_ELEMENT_BUTTON);
	}
	
	public void clickAddElementButton(){
		getAddElementButton().click();
		getAddElementButton().waitForVaadin();
	}

	public void addElement(int row) {
		selectElement(row);
		clickAddElementButton();
	}
	
	public void doubleClickOnToken(int token){
		$(PanelElement.class).$(HorizontalLayoutElement.class).$$(CssLayoutElement.class).get(token).doubleClick();
	}
	
	public SelectAnElement getSelectAnElement() {
		return selectAnElement;
	}

	public void addVariable(String name) {
		selectVariable(name);
		clickAddVariableButton();
	}

	private void selectVariable(String name) {
		getVariablesTable().selectByText(name);
	}

	private ListSelectElement getVariablesTable() {
		return $(ListSelectElement.class).caption(VARIABLES_TABLE).first();
	}

	private void clickAddVariableButton() {
		getAddVariableButton();
	}

	private void getAddVariableButton() {
		getButtonByCaption(ADD_VARIABLE_BUTTON).click();
		getButtonByCaption(ADD_VARIABLE_BUTTON).waitForVaadin();
	}

	public SelectAVariable getSelectAVariable() {
		return selectAVariable;
	}

	public void changeToTab(int tab) {
		$(TabSheetElement.class).first().openTab(tab);
	}

	public void selectGenericElement(int row) {
		getGenericElementTable().getCell(row, 0);
	}

	private TreeTableElement getGenericElementTable() {
		return $(TreeTableElement.class).caption(GENERIC_ELEMENT_TABLE).first();
	}

	public void addGenericElement(int row) {
		selectGenericElement(row);
		clickAddGenericElementButton();
	}

	private void clickAddGenericElementButton() {
		getAddGenericElementButton().click();
		getAddGenericElementButton().waitForVaadin();
	}

	private ButtonElement getAddGenericElementButton() {
		return getButtonByCaption(ADD_GENERIC_ELEMENT_BUTTON);
	}
	
}
