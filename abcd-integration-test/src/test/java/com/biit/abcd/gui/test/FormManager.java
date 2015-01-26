package com.biit.abcd.gui.test;

import com.biit.abcd.gui.test.window.NewForm;
import com.biit.abcd.gui.test.window.Proceed;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private final static String SETTINGS_BUTTON = "settingsButton";
	private final static String LOGOUT_BUTTON = "logoutButton";
	
	private final NewForm newFormWindow;
	private final Proceed proceed;
	
	public FormManager() {
		super();
		newFormWindow = new NewForm();
		proceed = new Proceed();
		addWindow(newFormWindow);
		addWindow(proceed);
	}

	protected void logOut() {
		$(ButtonElement.class).id(SETTINGS_BUTTON).click();
		$(ButtonElement.class).id(LOGOUT_BUTTON).click();
	}
	
	private ButtonElement getNewMenu(){
		 return $(ButtonElement.class).caption("New").first();
	}
	
	public ButtonElement getNewForm(){
		getNewMenu().click();
		return $(ButtonElement.class).caption("Form").first();
	}
	
	public ButtonElement getRemoveForm(){
		return $(ButtonElement.class).caption("Remove Form").first();
	}
	
	private void openNewForm(){
		getNewForm().click();
	}
	
	public void createNewForm(String formName){
		openNewForm();
		newFormWindow.createNewForm(formName);
	}
	
	public TreeTableElement getFormTable(){
		return $(TreeTableElement.class).first();
	}

	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteForm(int row) {
		getFormTable().getRow(row).click();
		getRemoveForm().click();
		proceed.clickAccept(); 
	}

}
