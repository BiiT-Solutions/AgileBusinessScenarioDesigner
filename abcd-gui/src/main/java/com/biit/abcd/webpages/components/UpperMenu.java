package com.biit.abcd.webpages.components;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class UpperMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "100px";
	public static final String SEPARATOR_WIDTH = "10px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";
	private static final String SEPARATOR_STYLE = "v-menu-separator";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private IconButton formManagerButton, settingsButton, droolsExporterButton;

	public UpperMenu() {
		super();
		this.defineUpperMenu();
		this.setContractIcons(true, BUTTON_WIDTH);
	}

	@Override
	protected void initHorizontalButtonGroup() {
		super.initHorizontalButtonGroup();

		this.upperRootLayout = new HorizontalLayout();
		this.upperRootLayout.setSizeFull();

		this.oldRootLayoutContainer = new HorizontalLayout();
		this.oldRootLayoutContainer.setSizeFull();
		this.oldRootLayoutContainer.setStyleName(CLASSNAME_HORIZONTAL_BUTTON_WRAPPER);

		// Add FormManager button.
		this.formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER, ThemeIcon.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
			private static final long serialVersionUID = 4002268252434768032L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
			}
		});
		this.formManagerButton.setEnabled(true);
		this.formManagerButton.setHeight("100%");
		this.formManagerButton.setWidth(BUTTON_WIDTH);

		CssLayout separator = new CssLayout();
		separator.setHeight("100%");
		separator.setWidth(SEPARATOR_WIDTH);
		separator.setStyleName(SEPARATOR_STYLE);

		this.settingsButton = new IconButton(LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, ThemeIcon.SETTINGS,
				LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, IconSize.BIG, new ClickListener() {
			private static final long serialVersionUID = 3450355943436017152L;

			@Override
			public void buttonClick(ClickEvent event) {
				SettingsWindow settings = new SettingsWindow();
				settings.showRelativeToComponent(UpperMenu.this.settingsButton);
			}
		});
		this.settingsButton.setHeight("100%");
		this.settingsButton.setWidth(BUTTON_WIDTH);

		this.droolsExporterButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_EXPORTER, ThemeIcon.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
			private static final long serialVersionUID = 4002268252434768032L;

			@Override
			public void buttonClick(ClickEvent event) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
				try {
					new Form2DroolsNoDrl().parse(UserSessionHandler.getFormController().getForm());
				} catch (ExpressionInvalidException | RuleInvalidException e) {
					MessageManager.showError(e.getMessage());
				}
				ps.close();
			}
		});
		this.droolsExporterButton.setEnabled(true);
		this.droolsExporterButton.setHeight("100%");
		this.droolsExporterButton.setWidth(BUTTON_WIDTH);

		Component currentRootLayout = this.getCompositionRoot();

		// First we change the composition root (vaadin component must be not in
		// the composition tree before adding it to another component)
		this.setCompositionRoot(this.upperRootLayout);
		this.oldRootLayoutContainer.addComponent(currentRootLayout);

		this.upperRootLayout.addComponent(this.oldRootLayoutContainer);
		this.upperRootLayout.addComponent(separator);
		this.upperRootLayout.addComponent(this.formManagerButton);
		this.upperRootLayout.addComponent(this.settingsButton);
		this.upperRootLayout.addComponent(this.droolsExporterButton);
		this.upperRootLayout.setExpandRatio(this.oldRootLayoutContainer, 1.0f);
		this.upperRootLayout.setExpandRatio(separator, 0.0f);
		this.upperRootLayout.setExpandRatio(this.droolsExporterButton, 0.0f);
		this.upperRootLayout.setExpandRatio(this.settingsButton, 0.0f);
		this.upperRootLayout.setExpandRatio(this.formManagerButton, 0.0f);

		this.setSizeFull();
	}

	private void defineUpperMenu() {
		this.setWidth("100%");
		this.setHeight("70px");
		this.setStyleName("upper-menu v-horizontal-button-group");
	}
}
