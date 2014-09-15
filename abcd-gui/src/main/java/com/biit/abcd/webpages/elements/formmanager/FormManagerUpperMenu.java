package com.biit.abcd.webpages.elements.formmanager;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.FormManager;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.DroolsSubmittedFormWindow;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class FormManagerUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 504419812975550794L;
	private IconButton newFormButton, exportToDrools;
	private FormManager parent;
	private List<IFormSelectedListener> formSelectedListeners;

	public FormManagerUpperMenu(FormManager parent) {
		super();
		this.parent = parent;
		formSelectedListeners = new ArrayList<>();
		defineMenu();
		setEnabledButtons();
	}

	private void defineMenu() {
		// Add new Form
		newFormButton = new IconButton(LanguageCodes.FORM_MANAGER_EDIT_FORM, ThemeIcon.FORM_MANAGER_ADD_FORM,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 6053447189295644721L;

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().addWindow(
								new WindowNewForm(parent, LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
										LanguageCodes.WINDOW_NEWFORM_NAME_TEXTFIELD));
					}
				});
		// Add new Form
		exportToDrools = new IconButton(LanguageCodes.FORM_MANAGER_EXPORT_RULES, ThemeIcon.FORM_MANAGER_EXPORT_RULES,
				LanguageCodes.FORM_MANAGER_EXPORT_RULES, IconSize.MEDIUM, new ClickListener() {
					private static final long serialVersionUID = 267803697670003444L;

					@Override
					public void buttonClick(ClickEvent event) {
						final DroolsSubmittedFormWindow droolsWindow = new DroolsSubmittedFormWindow();
						droolsWindow.addAcceptActionListener(new AcceptActionListener() {

							@Override
							public void acceptAction(AcceptCancelWindow window) {
								// Accept button update the form from the simpleViewForm.
								launchListeners();
								// After this SaveAsButton standard behavior is launched automatically.
							}
						});
						droolsWindow.showCentered();
					}
				});
		addIconButton(newFormButton);
		addIconButton(exportToDrools);
	}

	public void setEnabledButtons() {
		newFormButton.setEnabled(AbcdAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), DActivity.FORM_CREATE));
	}

	private void launchListeners() {
		for (IFormSelectedListener listener : formSelectedListeners) {
			listener.formSelected();
		}
	}

	public void addFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.add(listener);
	}

	public void removeFormSelectedListener(IFormSelectedListener listener) {
		formSelectedListeners.remove(listener);
	}
}
