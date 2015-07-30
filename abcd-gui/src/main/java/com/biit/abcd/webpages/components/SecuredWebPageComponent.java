package com.biit.abcd.webpages.components;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.WebMap;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;

/**
 * Before entering to the web page, the system checks user permissions to allow the access or redirect to another page.
 */
public abstract class SecuredWebPageComponent extends WebPageComponent {
	private static final long serialVersionUID = 1948083638306683637L;

	private IAbcdFormAuthorizationService securityService;

	protected SecuredWebPageComponent() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	public void securedEnter(ViewChangeEvent event) {
		initContent();
	}

	/**
	 * For increasing the performance, any class inheriting this, must defined the creation of the components inside the
	 * securedEnter method. Is recommended then, that the rootlayout is inserted the last one to avoid multiple
	 * communications with the client, i.e. add any component, button to the rootlayout and the last operation is
	 * getWorkingAreaLayout().addComponent(rootLayout);
	 */
	protected abstract void initContent();

	@Override
	public final void enter(ViewChangeEvent event) {
		// Check if the user is logged in. If not, redirect to main page.
		try {
			IUser<Long> user = UserSessionHandler.getUser();
			if (user == null) {
				AbcdLogger.info(this.getClass().getName(),
						"Unknown user has tried to enter a secured webpage. Navigated to Login page.");
				ApplicationFrame.navigateTo(WebMap.getLoginPage());
			} else {
				try {
					if (accessAuthorizationsRequired() != null && !accessAuthorizationsRequired().isEmpty()) {
						for (AbcdActivity activity : accessAuthorizationsRequired()) {
							if (!securityService.isUserAuthorizedInAnyOrganization(user, activity)) {
								MessageManager.showError(LanguageCodes.ERROR_NOT_AUTHORIZED);
								AbcdLogger.warning(this.getClass().getName(), "Activity '" + activity
										+ "' not authorized for user '" + user.getEmailAddress()
										+ "'. Returned to login screen.");
								ApplicationFrame.navigateTo(WebMap.getLoginPage());
							}
						}
					}
					securedEnter(event);
				} catch (UserManagementException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showError(LanguageCodes.ERROR_USER_SERVICE);
				}
			}
		} catch (NullPointerException npe) {
			AbcdLogger.errorMessage(this.getClass().getName(), npe);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.getErrorPage());
		}
	}

	/**
	 * Authorization required to access to this page. If user is not allowed, it will be redirect to login screen.
	 * 
	 * @return
	 */
	public abstract List<AbcdActivity> accessAuthorizationsRequired();

}
