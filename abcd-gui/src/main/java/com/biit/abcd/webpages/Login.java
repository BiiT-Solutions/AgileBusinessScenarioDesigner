package com.biit.abcd.webpages;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.http.client.ClientProtocolException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.WebPageComponent;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
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
import com.vaadin.ui.UI;
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
		if (((ApplicationFrame) getUI()).getUser() != null && ((ApplicationFrame) getUI()).getUser().length() > 0
				&& ((ApplicationFrame) getUI()).getPassword() != null
				&& ((ApplicationFrame) getUI()).getPassword().length() > 0) {
			AbcdLogger.info(this.getClass().getName(), "Autologin with user '" + ((ApplicationFrame) getUI()).getUser()
					+ "' and password with length of " + ((ApplicationFrame) getUI()).getPassword().length());
			try {
				User user = checkUserAndPassword(((ApplicationFrame) getUI()).getUser(),
						((ApplicationFrame) getUI()).getPassword());
				if (user != null) {
					// Try to go to the last page and last form if uses has no logged out.
					if (UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()) != null) {
						UserSessionHandler.restoreUserSession();
						ApplicationFrame.navigateTo(UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()));
					} else {
						ApplicationFrame.navigateTo(WebMap.getMainPage());
					}
				}
			} catch (InvalidCredentialsException | NotConnectedToWebServiceException | PBKDF2EncryptorException
					| IOException | AuthenticationRequired | WebServiceAccessError e) {
				AbcdLogger.info(this.getClass().getName(),
						"Autologin with user '" + ((ApplicationFrame) getUI()).getUser()
								+ "' failed! Wrong user or password.");
			}
		} else {
			if (((ApplicationFrame) getUI()).getUser() != null && ((ApplicationFrame) getUI()).getUser().length() > 0) {
				AbcdLogger.info(this.getClass().getName(),
						"Autologin with user '" + ((ApplicationFrame) getUI()).getUser()
								+ "' but no password provided!");
			}
		}
	}

	private Panel buildLoginForm() {
		Panel panel = new Panel();
		panel.setSizeUndefined();

		// Create input fields for user name and password
		usernameField = new TextField(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_EMAIL));
		usernameField.setRequired(true);
		usernameField.setRequiredError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_EMAIL));
		usernameField.setWidth(FIELD_SIZE);
		usernameField.focus();
		usernameField.setId("userNameLoginForm");

		passwordField = new PasswordField(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_PASSWORD));
		passwordField.setRequired(true);
		passwordField.setWidth(FIELD_SIZE);
		passwordField.setRequiredError(ServerTranslate.translate(LanguageCodes.LOGIN_ERROR_PASSWORD));
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
						User user = checkUserAndPassword(usernameField.getValue(), passwordField.getValue());
						if (user != null) {
							ApplicationFrame.navigateTo(WebMap.getMainPage());
						}
					} catch (InvalidCredentialsException | AuthenticationRequired e) {
						passwordField.setComponentError(new UserError(ServerTranslate.translate(
								LanguageCodes.LOGIN_ERROR_USER, new Object[] { usernameField.getValue() })));
						MessageManager.showError(LanguageCodes.ERROR_BADUSERPSWD, LanguageCodes.ERROR_TRYAGAIN);
					} catch (IOException | WebServiceAccessError | NotConnectedToWebServiceException e) {
						e.printStackTrace();
						AbcdLogger.errorMessage(this.getClass().getName(), e);
						MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE, LanguageCodes.ERROR_CONTACT);
					} catch (PBKDF2EncryptorException e) {
						MessageManager.showError(LanguageCodes.ERROR_ENCRYPTINGPASSWORD, LanguageCodes.ERROR_CONTACT);
					}
				}
				// If write user name and press enter, go to pass field.
				if (target == usernameField) {
					passwordField.focus();
				}
			}
		});

		// Add the login button
		Button loginButton = new Button(ServerTranslate.translate(LanguageCodes.LOGIN_CAPTION_SIGN_IN),
				new ClickListener() {
					private static final long serialVersionUID = 1239035599265918788L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							User user = checkUserAndPassword(usernameField.getValue(), passwordField.getValue());
							if (user != null) {
								ApplicationFrame.navigateTo(WebMap.getMainPage());
							}
						} catch (InvalidCredentialsException | AuthenticationRequired e) {
							passwordField.setComponentError(new UserError(ServerTranslate.translate(
									LanguageCodes.LOGIN_ERROR_USER, new Object[] { usernameField.getValue() })));
							MessageManager.showError(LanguageCodes.ERROR_BADUSERPSWD, LanguageCodes.ERROR_TRYAGAIN);
						} catch (IOException | WebServiceAccessError | NotConnectedToWebServiceException e) {
							e.printStackTrace();
							AbcdLogger.errorMessage(this.getClass().getName(), e);
							MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE, LanguageCodes.ERROR_CONTACT);
						} catch (PBKDF2EncryptorException e) {
							MessageManager.showError(LanguageCodes.ERROR_ENCRYPTINGPASSWORD,
									LanguageCodes.ERROR_CONTACT);
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

	private User checkUserAndPassword(String userMail, String password) throws JsonParseException,
			JsonMappingException, ClientProtocolException, InvalidCredentialsException,
			NotConnectedToWebServiceException, PBKDF2EncryptorException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		User user = AuthenticationService.getInstance().authenticate(userMail, password);

		if (user != null) {
			WebBrowser browser = UI.getCurrent().getPage().getWebBrowser();
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
			UserSessionHandler.checkOnlyOneSession(user, UI.getCurrent(), browser.getAddress());
		}
		return user;
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
