package com.biit.abcd;

import java.io.IOException;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.WebMap;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.liferay.portal.model.User;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("abcd")
@PreserveOnRefresh
public class ApplicationFrame extends UI {
	public final static String USER_PARAMETER_TAG = "user";
	public final static String PASSWORD_PARAMETER_TAG = "password";
	private static final long serialVersionUID = -704009283476930001L;
	private Navigator navigator;
	private View currentView;
	private String userEmail;
	private String password;

	private class AbcdErrorHandler extends DefaultErrorHandler {
		private static final long serialVersionUID = -5570064834518413901L;

		@Override
		public void error(com.vaadin.server.ErrorEvent event) {
			// Throw the error to the logger.
			AbcdLogger.errorMessage(ApplicationFrame.class.getName(), event.getThrowable());
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
		}
	};

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");
		defineWebPages();

		// Liferay send this data and automatically are used in the login screen.
		this.userEmail = request.getParameter(USER_PARAMETER_TAG);
		this.password = request.getParameter(PASSWORD_PARAMETER_TAG);

		setErrorHandler(new AbcdErrorHandler());

		autologin(userEmail, password);
	}

	private void autologin(String userEmail, String password) {
		// When accessing from Liferay, user and password are already set.
		if (userEmail != null && userEmail.length() > 0 && password != null && password.length() > 0) {
			AbcdLogger.info(this.getClass().getName(), "Autologin with user '" + userEmail
					+ "' and password with length of " + password.length());
			try {
				User user = UserSessionHandler.getUser(userEmail, password);
				if (user != null) {
					// Try to go to the last page and last form if user has no logged out.
					if (UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()) != null) {
						UserSessionHandler.restoreUserSession();
						navigateTo(UserSessionHandler.getUserLastPage(UserSessionHandler.getUser()));
					} else {
						navigateTo(WebMap.getMainPage());
					}
				}
			} catch (InvalidCredentialsException | NotConnectedToWebServiceException | PBKDF2EncryptorException
					| IOException | AuthenticationRequired | WebServiceAccessError e) {
				AbcdLogger.info(this.getClass().getName(), "Autologin with user '" + userEmail
						+ "' failed! Wrong user or password.");
			}
		} else {
			if (userEmail != null && userEmail.length() > 0) {
				AbcdLogger.info(this.getClass().getName(), "Autologin with user '" + userEmail
						+ "' but no password provided!");
			}
		}
	}

	@Override
	public void detach() {
		releaseResources();
		super.detach();
	}

	private void releaseResources() {
		if (UserSessionHandler.getUser() != null) {
			User user = UserSessionHandler.getUser();
			// Log user UI expired.
			AbcdLogger.info(this.getClass().getName(), user.getEmailAddress() + " UI has expired.");
			UiAccesser.releaseForm(user);
			// UserSessionHandler.logout(user);
		}
	}

	private void setChangeViewEvents() {
		navigator.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = -668206181478591694L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				setCurrentView(event.getNewView());
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				if (UserSessionHandler.getUser() != null) {
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has change view to '"
							+ event.getNewView().getClass().getName() + "'.");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void defineWebPages() {
		// Create a navigator to control the views
		navigator = new Navigator(this, this);
		// Define login page as first one.
		navigator.addView("", WebMap.getLoginPage().getWebPageJavaClass());
		navigator.setErrorView(WebMap.getLoginPage().getWebPageJavaClass());
		// Create and register the other web pages.
		for (WebMap page : WebMap.values()) {
			addView(page);
		}
		setChangeViewEvents();
	}

	@SuppressWarnings("unchecked")
	private void addView(WebMap newPage) {
		navigator.addView(newPage.toString(), newPage.getWebPageJavaClass());
	}

	public static void navigateTo(WebMap newPage) {
		UI.getCurrent().getNavigator().navigateTo(newPage.toString());
		UserSessionHandler.setUserLastPage(newPage);
	}

	public View getCurrentView() {
		return currentView;
	}

	private void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getPassword() {
		return password;
	}

}
