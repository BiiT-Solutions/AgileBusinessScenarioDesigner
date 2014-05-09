package com.biit.abcd.webpages.components;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.WebMap;
import com.liferay.portal.model.User;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public abstract class SecuredWebPageComponent extends WebPageComponent {
	
	public abstract void securedEnter(ViewChangeEvent event);

	@Override
	public final void enter(ViewChangeEvent event) {
		// Check if the user is logged in. If not, redirect to main page.
		try {
			User user = UserSessionHandler.getUser();
			if (user == null) {
				ApplicationFrame.navigateTo(WebMap.getLoginPage());
			} else {
				if (!AbcdAuthorizationService.getInstance().isAuthorizedActivity(user, DActivity.READ)) {
					MessageManager.showWarning("Access denied.", "User '" + user.getScreenName()
							+ "' has not the required permissions to use the application.");
					ApplicationFrame.navigateTo(WebMap.getLoginPage());
					// For security avoid access if an user type the url of this
					// page.
				} else if (accessAuthorizationsRequired() != null && !accessAuthorizationsRequired().isEmpty()) {
					for (DActivity activity : accessAuthorizationsRequired()) {
						if (!AbcdAuthorizationService.getInstance().isAuthorizedActivity(user, activity)) {
							ApplicationFrame.navigateTo(WebMap.getLoginPage());
						}
					}
				}
			}
		} catch (NullPointerException npe) {
			ApplicationFrame.navigateTo(WebMap.getLoginPage());
		}
		
		securedEnter(event);
	}
	
	

	/**
	 * Authorization required to access to this page. If user is not allowed, it will be redirect to login screen.
	 * 
	 * @return
	 */
	public abstract List<DActivity> accessAuthorizationsRequired();

}
