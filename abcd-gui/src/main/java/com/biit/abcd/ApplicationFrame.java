package com.biit.abcd;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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
	private String user;
	private String password;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");
		defineWebPages();

		//Liferay send this data and automatically are used in the login screen. 
		this.user = request.getParameter(USER_PARAMETER_TAG);
		this.password = request.getParameter(PASSWORD_PARAMETER_TAG);
	}

	@Override
	public void detach() {
		releaseResources();
		super.detach();
	}

	private void releaseResources() {
		if (UserSessionHandler.getUser() != null) {
			// Log user UI expired.
			AbcdLogger.info(this.getClass().getName(), UserSessionHandler.getUser().getEmailAddress()
					+ " UI has expired.");
			UiAccesser.releaseForm(UserSessionHandler.getUser());
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
	}

	public View getCurrentView() {
		return currentView;
	}

	private void setCurrentView(View currentView) {
		this.currentView = currentView;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
