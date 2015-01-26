package com.biit.abcd.gui.test;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class Login extends VaadinGuiWebpage{

	private final static String LOGIN_URL = "http://localhost:9081";
	private final static String USER_NAME_LOGIN_FORM = "userNameLoginForm";
	private final static String USER_PASS_LOGIN_FORM = "userPassLoginForm";
	
	private TextFieldElement getUserNameLoginForm(){
		return $(TextFieldElement.class).id(USER_NAME_LOGIN_FORM);
	}
	
	private PasswordFieldElement getUserPassLoginForm(){
		return $(PasswordFieldElement.class).id(USER_PASS_LOGIN_FORM);
	}
	
	private ButtonElement getLoginButton(){
		return $(ButtonElement.class).id("loginButton");
	}
	
	public void login(String userName, String password){
		getUserNameLoginForm().setValue(userName);
		getUserPassLoginForm().setValue(password);
		getLoginButton().click();
	}

	@Override
	public String getWebpageUrl() {
		return LOGIN_URL;
	}
}
