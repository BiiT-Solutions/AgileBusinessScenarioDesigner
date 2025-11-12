package com.biit.abcd.webpages;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.WebPageComponent;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class Login extends WebPageComponent {
	private static final long serialVersionUID = 1559169232291159835L;
	private static String FIELD_SIZE = "160px";
	private VerticalLayout rootLayout;
	private TextField usernameField;
	private PasswordField passwordField;

	public Login() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();

		VerticalLayout loginVersion = new VerticalLayout();
		loginVersion.setSizeUndefined();
		loginVersion.setSpacing(true);

		Panel loginPanel = buildLoginForm();
		Component nameVersion = createNameVersion();

		loginVersion.addComponent(loginPanel);
		loginVersion.addComponent(nameVersion);
		loginVersion.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		loginVersion.setComponentAlignment(nameVersion, Alignment.MIDDLE_CENTER);

		rootLayout.addComponent(loginVersion);
		rootLayout.setComponentAlignment(loginVersion, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Nothing to do. Autologin managed by ApplicationUI.
		ApplicationFrame.autologin();
	}

	private Panel buildLoginForm() {
		Panel panel = new Panel();
		panel.setSizeUndefined();

		// Create input fields for user name and password
		usernameField = new TextField(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_EMAIL));
		usernameField.setRequired(true);
		usernameField.setWidth(FIELD_SIZE);
		usernameField.focus();
		usernameField.setId("userNameLoginForm");

		passwordField = new PasswordField(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_PASSWORD));
		passwordField.setRequired(true);
		passwordField.setWidth(FIELD_SIZE);
		passwordField.setId("userPassLoginForm");

		// If you press enter. Login operation.
		passwordField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -3780782610097189332L;

			@Override
			public void handleAction(Object sender, Object target) {
				// limit the enters to only from the password field from this
				// form
				if (target == passwordField) {
					try {
						IUser<Long> user = UserSessionHandler.getUser(usernameField.getValue(), passwordField.getValue());
						if (user != null) {
							ApplicationFrame.navigateTo(WebMap.getMainPage());
						}
					} catch (InvalidCredentialsException | UserDoesNotExistException e) {
						passwordField.setComponentError(new UserError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_USER,
								new Object[] { usernameField.getValue() })));
						MessageManager.showError(LanguageCodes.ERROR_BADUSERPSWD, LanguageCodes.ERROR_TRYAGAIN);
					} catch (UserManagementException | AuthenticationRequired e) {
						AbcdLogger.errorMessage(this.getClass().getName(), e);
						MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE, LanguageCodes.ERROR_CONTACT);
					}
				}
				// If write user name and press enter, go to pass field.
				if (target == usernameField) {
					passwordField.focus();
				}
			}
		});

		// Add the login button
		Button loginButton = new Button(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_SIGN_IN), new ClickListener() {
			private static final long serialVersionUID = 1239035599265918788L;

			@Override
			public void buttonClick(ClickEvent event) {
				usernameField.setRequiredError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_EMAIL));
				passwordField.setRequiredError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_PASSWORD));
				try {
					IUser<Long> user = UserSessionHandler.getUser(usernameField.getValue(), passwordField.getValue());
					if (user != null) {
						ApplicationFrame.navigateTo(WebMap.getMainPage());
					}
				} catch (InvalidCredentialsException | UserDoesNotExistException e) {
					passwordField.setComponentError(new UserError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_USER,
							new Object[] { usernameField.getValue() })));
					MessageManager.showError(LanguageCodes.ERROR_BADUSERPSWD, LanguageCodes.ERROR_TRYAGAIN);
				} catch (UserManagementException | AuthenticationRequired e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE, LanguageCodes.ERROR_CONTACT);
				}
			}
		});
		loginButton.setWidth(FIELD_SIZE);
		loginButton.setId("loginButton");

		// Alignment and sizes.
		FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSizeUndefined();
		layout.addComponent(usernameField);
		layout.addComponent(passwordField);
		layout.addComponent(loginButton);
		panel.setContent(layout);
		return panel;
	}

	private Component createNameVersion() {
		Label label = new Label("Agile Business sCenario Designer - v" + getVersion());
		label.setWidth(null);
		return label;
	}

	private String getVersion() {
		ServletContext context = VaadinServlet.getCurrent().getServletContext();
		Manifest manifest;
		String version = null;
		try {
			manifest = new Manifest(context.getResourceAsStream("/META-INF/MANIFEST.MF"));
			Attributes attributes = manifest.getMainAttributes();
			version = attributes.getValue("Implementation-Version");
		} catch (IOException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return version;
	}
}
