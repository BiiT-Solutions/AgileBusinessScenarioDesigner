package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.abcd.webpages.WebMap;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public abstract class UpperMenu extends SecuredMenu {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "110px";
	public static final String SEPARATOR_WIDTH = "10px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";
	private static final String SEPARATOR_STYLE = "v-menu-separator";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private IconButton formManagerButton, settingsButton;

	public UpperMenu() {
		super();
		defineUpperMenu();
		this.setContractIcons(true, BUTTON_WIDTH);
	}

	@Override
	protected void initHorizontalButtonGroup() {
		super.initHorizontalButtonGroup();

		upperRootLayout = new HorizontalLayout();
		upperRootLayout.setSizeFull();

		oldRootLayoutContainer = new HorizontalLayout();
		oldRootLayoutContainer.setSizeFull();
		oldRootLayoutContainer.setStyleName(CLASSNAME_HORIZONTAL_BUTTON_WRAPPER);

		// Add FormManager button.
		formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER, ThemeIcon.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 4002268252434768032L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							UserSessionHandler.getFormController().checkUnsavedChanges();
							UiAccesser.releaseForm(UserSessionHandler.getUser());
							ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
						} catch (TreeObjectNotEqualsException | StorableObjectNotEqualsException
								| FormNotEqualsException | GroupNotEqualsException | QuestionNotEqualsException
								| CustomVariableNotEqualsException | ExpressionNotEqualsException
								| TableRuleNotEqualsException | RuleNotEqualsException | DiagramNotEqualsException
								| DiagramObjectNotEqualsException | NodeNotEqualsException | SizeNotEqualsException
								| PointNotEqualsException | BiitTextNotEqualsException
								| GlobalVariableNotEqualsException | VariableDataNotEqualsException e) {
							final AlertMessageWindow windowAccept = new AlertMessageWindow(
									LanguageCodes.WARNING_LOST_UNSAVED_DATA);
							windowAccept.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									UiAccesser.releaseForm(UserSessionHandler.getUser());
									ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
									windowAccept.close();
								}
							});
							windowAccept.showCentered();
						}
					}
				});
		formManagerButton.setEnabled(true);
		formManagerButton.setHeight("100%");
		formManagerButton.setWidth(BUTTON_WIDTH);

		CssLayout separator = new CssLayout();
		separator.setHeight("100%");
		separator.setWidth(SEPARATOR_WIDTH);
		separator.setStyleName(SEPARATOR_STYLE);

		settingsButton = new IconButton(LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, ThemeIcon.SETTINGS,
				LanguageCodes.TOP_MENU_SETTINGS_TOOLTIP, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 3450355943436017152L;

					@Override
					public void buttonClick(ClickEvent event) {
						SettingsWindow settings = new SettingsWindow();
						settings.showRelativeToComponent(settingsButton);
					}
				});
		settingsButton.setHeight("100%");
		settingsButton.setWidth(BUTTON_WIDTH);

		Component currentRootLayout = getCompositionRoot();

		// First we change the composition root (vaadin component must be not in
		// the composition tree before adding it to another component)
		setCompositionRoot(upperRootLayout);
		oldRootLayoutContainer.addComponent(currentRootLayout);

		upperRootLayout.addComponent(oldRootLayoutContainer);
		upperRootLayout.addComponent(separator);
		upperRootLayout.addComponent(formManagerButton);
		upperRootLayout.addComponent(settingsButton);
		upperRootLayout.setExpandRatio(oldRootLayoutContainer, 1.0f);
		upperRootLayout.setExpandRatio(separator, 0.0f);
		upperRootLayout.setExpandRatio(settingsButton, 0.0f);
		upperRootLayout.setExpandRatio(formManagerButton, 0.0f);

		setSizeFull();
	}

	private void defineUpperMenu() {
		this.setWidth("100%");
		this.setHeight("70px");
		setStyleName("upper-menu v-horizontal-button-group");
	}
}
