package com.biit.abcd.authentication;

import java.util.HashMap;

import com.biit.abcd.MessageManager;
import com.biit.abcd.core.FormController;
import com.biit.abcd.core.GlobalVariablesController;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.TestScenarioController;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.WebMap;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private User user = null;
	private FormController formController;
	private static GlobalVariablesController globalVariablesController;
	private static TestScenarioController testScenariosController;
	// User Id --> UI
	private static HashMap<Long, UI> usersSession = new HashMap<>();

	/**
	 * Initializes the {@link UserSessionHandler} for the given {@link Application}
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new UserSessionHandler(ui);
	}

	public static void checkOnlyOneSession(User user, UI ui) {
		if (usersSession.get(user.getUserId()) != null) {
			//usersSession.get(user.getUserId()).getNavigator().navigateTo(WebMap.LOGIN_PAGE.toString());
			System.out.println(usersSession.get(user.getUserId()).getConnectorId() + " --> " + ui.getConnectorId());
			System.out.println(usersSession.get(user.getUserId()).equals(ui));
			usersSession.get(user.getUserId()).close();
			MessageManager.showWarning(LanguageCodes.INFO_USER_SESSION_EXPIRED);
		}
		usersSession.put(user.getUserId(), ui);
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
	public static void setUser(User user) {
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
	public static User getUser() {
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
	 * Set the User object for the currently inlogged user for this application instance
	 * 
	 * @param user
	 */
	public static void login(User user) {
		setUser(user);
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		if (getUser() != null) {
			usersSession.remove(getUser().getUserId());
		}
		setUser(null);
	}

	public static GlobalVariablesController getGlobalVariablesController() {
		if (globalVariablesController == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			globalVariablesController = new GlobalVariablesController(helper);
		}
		return globalVariablesController;
	}

	public static TestScenarioController getTestScenariosController() {
		if (testScenariosController == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			testScenariosController = new TestScenarioController(helper);
		}
		return testScenariosController;
	}
}
