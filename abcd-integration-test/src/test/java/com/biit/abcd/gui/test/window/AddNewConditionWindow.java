package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class AddNewConditionWindow extends AcceptCancelWindow{
	
	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.AddNewQuestionEditorWindow";
	private static final String SELECT_AN_ELEMENT_CAPTION = "Select an Element:";
	private static final String SELECT_A_VARIABLE_CAPTION = "Select a Variable:";

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}
	
	public void selectAnElement(int row){
		getSelectAnElement().getCell(row, 0).click();
		getSelectAnElement().getCell(row, 0).waitForVaadin();
		clickAccept();
	}
	
	public void selectAVariable(int row, String name){
		getSelectAnElement().getCell(row, 0).click();
		getSelectAnElement().getCell(row, 0).waitForVaadin();
		getSelectAVariable().selectByText(name);
		getSelectAVariable().waitForVaadin();
		clickAccept();
	}
	
	public TreeTableElement getSelectAnElement(){
		 return getWindow().$(TreeTableElement.class).caption(SELECT_AN_ELEMENT_CAPTION).first();
	}
	
	public ListSelectElement getSelectAVariable(){
		return getWindow().$(ListSelectElement.class).caption(SELECT_A_VARIABLE_CAPTION).first();
	}

	public void expandElement(int row) {
		getSelectAnElement().getRow(row).toggleExpanded();
	}

}
