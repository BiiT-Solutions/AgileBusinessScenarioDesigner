package com.biit.abcd.gui.test.webpage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.window.NewRuleExpressionWindow;
import com.biit.abcd.gui.test.window.SelectAVariable;
import com.biit.abcd.gui.test.window.SelectAnElement;
import com.biit.abcd.gui.test.window.SelectGenericVariable;
import com.biit.abcd.gui.test.window.SelectGlobalConstantWindow;
import com.biit.abcd.gui.test.window.StringInputWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.PanelElement;
import com.vaadin.testbench.elements.TabSheetElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class RuleExpression extends LeftTreeTableWebpage {

	private static final String EXPRESSION_TABLE_CAPTION = "expression-table";
	private static final String ADD_ELEMENT_BUTTON = "Add Element";
	private static final String ADD_VARIABLE_BUTTON = "Add Variable";
	private static final String VARIABLES_TABLE = "Select a Variable:";
	private static final String GENERIC_ELEMENT_TABLE = "Select a Generic Element:";
	private static final String ADD_GENERIC_ELEMENT_BUTTON = "Add Generic Element";
	private static final String GENERIC_VARIABLE_TABLE = "Select a Generic Variable:";
	private static final String ADD_GENERIC_VARIABLE_BUTTON = "Add Generic Variable";
	private static final String INPUT_VALUE_BUTTON = "Input Value";
	private static final Object VALID_CAPTION = "Valid";
	private final NewRuleExpressionWindow newRuleExpressionWindow;
	private final SelectAnElement selectAnElement;
	private final SelectAVariable selectAVariable;
	private final SelectGenericVariable selectGenericVariable;
	private final StringInputWindow stringInputWindow;
	private final SelectGlobalConstantWindow selectGlobalConstantWindow;

	public RuleExpression() {
		super();
		newRuleExpressionWindow = new NewRuleExpressionWindow();
		selectAnElement = new SelectAnElement();
		selectAVariable = new SelectAVariable();
		selectGenericVariable = new SelectGenericVariable();
		stringInputWindow = new StringInputWindow();
		selectGlobalConstantWindow = new SelectGlobalConstantWindow();
		addWindow(newRuleExpressionWindow);
		addWindow(selectAnElement);
		addWindow(selectAVariable);
		addWindow(selectGenericVariable);
		addWindow(stringInputWindow);
		addWindow(selectGlobalConstantWindow);
	}

	public void newRuleExpression(String name) {
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

	public void selectElement(int row) {	
		getSelectElementTable().getCell(row, 0).click();
		getSelectElementTable().getCell(row, 0).waitForVaadin();
	}

	public void toggleSelectElementRow(int row) {
		getSelectElementTable().getRow(row).toggleExpanded();
	}

	public ButtonElement getAddElementButton() {
		return getButtonByCaption(ADD_ELEMENT_BUTTON);
	}

	public void clickAddElementButton() {
		getAddElementButton().click();
		getAddElementButton().waitForVaadin();
	}

	public void addElement(int row) {
		selectElement(row);
		clickAddElementButton();
	}

	public void doubleClickOnToken(int token) {
		$(PanelElement.class).$(HorizontalLayoutElement.class).$$(CssLayoutElement.class).get(token).doubleClick();
	}
	
	public CssLayoutElement getToken(int token){
		return $(PanelElement.class).$(HorizontalLayoutElement.class).$$(CssLayoutElement.class).get(token);
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
		getAddVariableButton().click();
		getAddVariableButton().waitForVaadin();
	}

	private ButtonElement getAddVariableButton() {
		return getButtonByCaption(ADD_VARIABLE_BUTTON);
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

	public void addGenericElementVariable(int row, String variable) {
		selectGenericElement(row);
		addGenericVariable(variable);
	}

	private void addGenericVariable(String variable) {
		selectGenericVariable(variable);
		clickAddGenericVariableButton();
	}

	private void selectGenericVariable(String name) {
		getGenericVariablesTable().selectByText(name);
	}

	private ListSelectElement getGenericVariablesTable() {
		return $(ListSelectElement.class).caption(GENERIC_VARIABLE_TABLE).first();
	}

	private void clickAddGenericVariableButton() {
		getAddGenericVariableButton().click();
		getAddGenericVariableButton().waitForVaadin();
	}

	private ButtonElement getAddGenericVariableButton() {
		return getButtonByCaption(ADD_GENERIC_VARIABLE_BUTTON);
	}

	public SelectGenericVariable getSelectGenericVariable() {
		return selectGenericVariable;
	}

	public void addInputValue(AnswerFormat answerFormat,String value) {
		getInputValue().click();
		getStringInputWindow().setType(answerFormat);
		getStringInputWindow().setValue(value);
		getStringInputWindow().clickAccept();
	}

	private ButtonElement getInputValue() {
		return getButtonByCaption(INPUT_VALUE_BUTTON);
	}
	
	public StringInputWindow getStringInputWindow() {
		return stringInputWindow;
	}

	public void editInputValue(int token, AnswerFormat answerFormat, String value) {
		doubleClickOnToken(token);
		getStringInputWindow().setType(answerFormat);
		getStringInputWindow().setValue(value);
		getStringInputWindow().clickAccept();
	}

	public void clickButton(String caption) {
		getButtonByCaption(caption).click();
	}

	public boolean isValid() {
		if($(HorizontalLayoutElement.class).$(LabelElement.class).first().getText().equals(VALID_CAPTION)){
			return true;
		}
		return false;
	}

	public SelectGlobalConstantWindow getSelectGlobalConstantWindow() {
		return selectGlobalConstantWindow;
	}
}
