package com.biit.abcd.webpages;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("abcd")
@PreserveOnRefresh
public class AutoLogin extends UI {
	public final static String USER_PARAMETER_TAG = "user";
	public final static String PASSWORD_PARAMETER_TAG = "password";
	private String user;
	private String password;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("AutoLogin");
		this.user = request.getParameter(USER_PARAMETER_TAG);
		this.password = request.getParameter(PASSWORD_PARAMETER_TAG);

		System.out.println("User " + user + " password: " + password);
	}
}
