package com.biit.abcd.authentication;

import com.biit.abcd.core.FormController;
import com.biit.abcd.core.GlobalVariablesController;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.TestScenariosController;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private User user = null;
	private FormController formController;
	private static GlobalVariablesController globalVariablesController;
	private static TestScenariosController testScenariosController;

	// Store the user object of the currently inlogged user

	/**
	 * Initializes the {@link UserSessionHandler} for the given
	 * {@link Application}
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new UserSessionHandler(ui);
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
	 * Set the User object for the currently inlogged user for this application
	 * instance
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
	 * Get the User object of the currently inlogged user for this application
	 * instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static User getUser() {
		UserSessionHandler session = getCurrent();
		return session.user;
	}

	/**
	 * Get the FormController object of the currently inlogged user for this
	 * application instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static FormController getFormController() {
		UserSessionHandler session = getCurrent();
		return session.formController;
	}

	/**
	 * Set the User object for the currently inlogged user for this application
	 * instance
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
		setUser(null);
	}

	public static GlobalVariablesController getGlobalVariablesController() {
		if (globalVariablesController == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			globalVariablesController = new GlobalVariablesController(helper);
		}
		return globalVariablesController;
	}

	public static TestScenariosController getTestScenariosController() {
		if (testScenariosController == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			testScenariosController = new TestScenariosController(helper);
		}
		return testScenariosController;
	}
}
