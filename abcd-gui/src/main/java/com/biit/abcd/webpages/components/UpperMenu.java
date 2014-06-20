package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class UpperMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "120px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private IconButton formManagerButton, settingsButton;

	public UpperMenu() {
		super();
		defineUpperMenu();
		setContractIcons(true, BUTTON_WIDTH);
	}

	protected void initHorizontalButtonGroup() {
		super.initHorizontalButtonGroup();

		upperRootLayout = new HorizontalLayout();
		upperRootLayout.setSizeFull();

		oldRootLayoutContainer = new HorizontalLayout();
		oldRootLayoutContainer.setSizeFull();
		oldRootLayoutContainer.setStyleName(CLASSNAME_HORIZONTAL_BUTTON_WRAPPER);

		// Add FormManager button.
		formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 4002268252434768032L;

					@Override
					public void buttonClick(ClickEvent event) {
						ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
					}
				});
		formManagerButton.setEnabled(true);
		formManagerButton.setHeight("100%");

		settingsButton = new IconButton(LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, ThemeIcons.SETTINGS,
				LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 3450355943436017152L;

					@Override
					public void buttonClick(ClickEvent event) {
						SettingsWindow settings = new SettingsWindow();
						settings.showRelativeToComponent(settingsButton);
					}
				});
		settingsButton.setHeight("100%");

		Component currentRootLayout = getCompositionRoot();

		// First we change the composition root (vaadin component must be not in
		// the composition tree before adding it to another component)
		setCompositionRoot(upperRootLayout);
		oldRootLayoutContainer.addComponent(currentRootLayout);

		upperRootLayout.addComponent(oldRootLayoutContainer);
		upperRootLayout.addComponent(formManagerButton);
		upperRootLayout.addComponent(settingsButton);
		upperRootLayout.setExpandRatio(oldRootLayoutContainer, 1.0f);
		upperRootLayout.setExpandRatio(settingsButton, 0.0f);
		upperRootLayout.setExpandRatio(formManagerButton, 0.0f);

		setSizeFull();
	}

	private void defineUpperMenu() {
		setWidth("100%");
		setHeight("70px");
		setStyleName("upper-menu v-horizontal-button-group");
	}
}
