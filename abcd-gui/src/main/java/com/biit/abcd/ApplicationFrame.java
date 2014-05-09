package com.biit.abcd;

import javax.servlet.annotation.WebServlet;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("abcd")
public class ApplicationFrame extends UI {
	private static final long serialVersionUID = -704009283476930001L;
	private Navigator navigator;
	private View currentView;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ApplicationFrame.class, widgetset = "com.biit.abcd.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = 6661236887598756889L;
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("");
		defineWebPages();
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

}
