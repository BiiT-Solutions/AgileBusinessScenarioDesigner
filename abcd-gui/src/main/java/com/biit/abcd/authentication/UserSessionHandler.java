package com.biit.abcd.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import com.biit.abcd.MessageManager;
import com.biit.abcd.core.FormController;
import com.biit.abcd.core.GlobalVariablesController;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.TestScenarioController;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.WebMap;
import com.biit.liferay.security.AuthenticationService;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private IUser<Long> user = null;
	private FormController formController;
	private static GlobalVariablesController globalVariablesController;
	private static TestScenarioController testScenariosController;
	// User Id --> List<UI> (A user can have different browsers opened in the
	// same machine)
	private static HashMap<Long, List<UI>> usersSession = new HashMap<>();
	// User Id --> IP (current UI ip connected)
	private static HashMap<Long, String> usersIp = new HashMap<>();
	// User Id --> Last Page visited
	private static HashMap<Long, WebMap> userLastPage = new HashMap<>();
	// User Id --> Last Form edited
	private static HashMap<Long, Form> userLastForm = new HashMap<>();

	/**
	 * Initializes the {@link UserSessionHandler} for the given application
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new UserSessionHandler(ui);
	}

	public static void checkOnlyOneSession(IUser<Long> user, UI ui, String ip) {
		if (usersSession.get(user.getId()) != null) {
			if (ip == null || !ip.equals(usersIp.get(user.getId()))) {
				closeSession(user);
				MessageManager.showWarning(LanguageCodes.INFO_USER_SESSION_EXPIRED);
			}
		}
		if (usersSession.get(user.getId()) == null) {
			usersSession.put(user.getId(), new ArrayList<UI>());
		}
		usersSession.get(user.getId()).add(ui);
		usersIp.put(user.getId(), ip);
	}

	/**
	 * Close all data related to a user.
	 * 
	 * @param user
	 */
	public static void closeSession(IUser<Long> user) {
		List<UI> uis = new ArrayList<>(usersSession.get(user.getId()));
		for (UI userUI : uis) {
			try {
				userUI.getNavigator().navigateTo(WebMap.getLoginPage().toString());
				userUI.close();
				usersSession.get(user.getId()).remove(userUI);
			} catch (Exception e) {
				// maybe the session has expired in Vaadin and cannot be closed.
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * @param ui
	 *            Current application instance
	 */
	public UserSessionHandler(UI ui) {
		ui.getSession().setAttribute(ui.getId() + DEFAULT_SUFIX, this);
	}

	private static UserSessionHandler getCurrent() {
		UI ui = UI.getCurrent();
		UserSessionHandler handler = (UserSessionHandler) ui.getSession().getAttribute(ui.getId() + DEFAULT_SUFIX);
		if (handler == null) {
			// Session Handler not created or expired. Create a new one.
			synchronized (UserSessionHandler.class) {
				new UserSessionHandler(ui);
				return getCurrent();
			}
		}
		return handler;
	}

	/**
	 * Set the User object for the currently inlogged user for this application instance
	 * 
	 * @param user
	 */
	public static void setUser(IUser<Long> user) {
		UserSessionHandler session = getCurrent();
		session.user = user;
		if (user != null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			session.formController = new FormController(user, helper);
		} else {
			session.formController = null;
		}
	}

	/**
	 * Get the User object of the currently inlogged user for this application instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static IUser<Long> getUser() {
		UserSessionHandler session = getCurrent();
		return session.user;
	}

	/**
	 * Get the FormController object of the currently inlogged user for this application instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static FormController getFormController() {
		UserSessionHandler session = getCurrent();
		return session.formController;
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		if (getUser() != null) {
			logout(getUser());
		}
		setUser(null);
	}

	public static void logout(IUser<Long> user) {
		usersSession.remove(user.getId());
		usersIp.remove(user.getId());
		userLastPage.remove(user.getId());
	}

	@FindBugsSuppressWarnings("DC_DOUBLECHECK")
	public static GlobalVariablesController getGlobalVariablesController() {
		if (globalVariablesController == null) {
			synchronized (UserSessionHandler.class) {
				if (globalVariablesController == null) {
					SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
					globalVariablesController = new GlobalVariablesController(helper);
				}
			}
		}
		return globalVariablesController;
	}

	@FindBugsSuppressWarnings("DC_DOUBLECHECK")
	public static TestScenarioController getTestScenariosController() {
		if (testScenariosController == null) {
			synchronized (UserSessionHandler.class) {
				if (testScenariosController == null) {
					SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
					testScenariosController = new TestScenarioController(helper);
				}
			}
		}
		return testScenariosController;
	}

	public static WebMap getUserLastPage(IUser<Long> user) {
		return userLastPage.get(user.getId());
	}

	public static void setUserLastPage(WebMap page) {
		setUserLastPage(getUser(), page);
	}

	public static void setUserLastPage(IUser<Long> user, WebMap page) {
		if (user != null) {
			if (!WebMap.getMainPage().equals(page) && !WebMap.getLoginPage().equals(page)) {
				userLastPage.put(user.getId(), page);
			} else {
				userLastPage.remove(user.getId());
			}
		}
	}

	private static void setLastForm(IUser<Long> user, Form form) {
		userLastForm.put(user.getId(), form);
	}

	public static void setForm(Form form) {
		if (getUser() != null) {
			setLastForm(getUser(), form);
			getFormController().setForm(form);
		}
	}

	private static Form getLastForm(IUser<Long> user) {
		return userLastForm.get(user.getId());
	}

	/**
	 * Sets the last form used by an user. This allows liferay to reload the last page visited.
	 */
	public static void restoreUserSession() {
		if (getUser() != null && getLastForm(getUser()) != null) {
			getFormController().setForm(getLastForm(getUser()));
		}
	}

	public static IUser<Long> getUser(String userMail, String password) throws UserManagementException,
			InvalidCredentialsException, AuthenticationRequired {
		// Try to log in the user when the button is clicked
		IUser<Long> user = AuthenticationService.getInstance().authenticate(userMail, password);

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
				AbcdLogger.info(UserSessionHandler.class.getName(), message);
			} catch (Exception e) {
				AbcdLogger.errorMessage(UserSessionHandler.class.getName(), e);
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
}
