package com.biit.abcd.authentication;

import com.liferay.portal.model.User;
import com.vaadin.ui.UI;

/**
 * Based on the appfoundation vaadin plugin.
 */
public class UserSessionHandler {
	private static final String DEFAULT_SUFIX = "-sessionhandler";
	private User user = null;

	// Store the user object of the currently inlogged user

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
	 * Set the User object for the currently inlogged user for this application instance
	 * 
	 * @param user
	 */
	public static void login(User user) {
		UserSessionHandler session = getCurrent();
		session.user = user;
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		setUser(null);
	}

}
