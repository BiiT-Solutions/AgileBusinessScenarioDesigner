package com.biit.abcd.webpages;

import java.io.IOException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.liferay.portal.model.User;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class Login extends VerticalLayout implements View {
	private static final long serialVersionUID = 1559169232291159835L;
	private TextField usernameField;
	private PasswordField passwordField;

	// Login is the only web page that does not use DWebPage as parent.

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();
		Panel loginPanel = buildLoginForm();
		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}

	private Panel buildLoginForm() {

		Panel panel = new Panel();
		panel.setSizeUndefined();

		// Create input fields for user name and password
		usernameField = new TextField("Email:");
		usernameField.setRequired(true);
		usernameField.setRequiredError("The Field may not be empty.");
		usernameField.focus();

		passwordField = new PasswordField("Password:");
		passwordField.setRequired(true);
		passwordField.setRequiredError("The Field may not be empty.");

		// If you press enter. Login operation.
		passwordField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -3780782610097189332L;

			@Override
			public void handleAction(Object sender, Object target) {
				// limit the enters to only from the password field from this
				// form
				if (target == passwordField) {
					checkUserAndPassword();
				}
				// If write user name and press enter, go to pass field.
				if (target == usernameField) {
					passwordField.focus();
				}
			}
		});

		// Add the login button
		// IconButton loginButton = new IconButton("Login", "user.png", "Check user.", new ClickListener() {
		//
		// private static final long serialVersionUID = -5577423546946890721L;
		//
		// public void buttonClick(ClickEvent event) {
		// checkUserAndPassword();
		// }
		// });

		// Alignment and sizes.
		FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSizeUndefined();
		layout.addComponent(usernameField);
		layout.addComponent(passwordField);
		// layout.addComponent(loginButton);
		panel.setContent(layout);
		return panel;
	}

	private void checkUserAndPassword() {
		// Try to log in the user when the button is clicked
		String userMail = (String) usernameField.getValue();
		String password = (String) passwordField.getValue();

		User user = null;
		try {
			user = AuthenticationService.getInstance().authenticate(userMail, password);
		} catch (InvalidCredentialsException | AuthenticationRequired e) {
			passwordField.setComponentError(new UserError("Username '" + userMail
					+ "' does not exists or password was wrong."));
			MessageManager.showError("Either username or password was wrong.", "Try again.");
		} catch (IOException | WebServiceAccessError | NotConnectedToWebServiceException e) {
			e.printStackTrace();
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError("Error connecting to Liferay user management service.",
					"Contact the software administrator.");
		} catch (PBKDF2EncryptorException e) {
			MessageManager.showError("Error encrypting the password.", "Contact the software administrator.");
		}

		if (user != null) {
			WebBrowser browser = (WebBrowser) UI.getCurrent().getPage().getWebBrowser();
			try {
				String message = "User '" + user.getEmailAddress() + "' logged successfully. Using '"
						+ browser.getBrowserApplication() + "'";
				if (browser.getAddress() != null) {
					message += " (IP: " + browser.getAddress() + ").";
				} else {
					message += ".";
				}
				AbcdLogger.info(this.getClass().getName(), message);
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			// Store the password.
			user.setPassword(password);

			// The user's password was correct, so set the user as the
			// current user (inlogged)
			UserSessionHandler.setUser(user);
			ApplicationFrame.navigateTo(WebMap.getMainPage());
		}
	}
}
