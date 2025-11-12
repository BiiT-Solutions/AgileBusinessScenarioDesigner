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
		getUserPassLoginForm().waitForVaadin();
		getLoginButton().focus();
		getLoginButton().waitForVaadin();
		getLoginButton().click();
	}

	@Override
	public String getWebpageUrl() {
		return LOGIN_URL;
	}
}
