package com.biit.abcd.webpages;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.abcd.webpages.components.WindowNewForm;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class FormManagerUpperMenu extends UpperMenu {
	private Button newFormButton;
	private FormManager parent;

	public FormManagerUpperMenu(FormManager parent) {
		super();
		this.parent = parent;
		defineMenu();
		setEnabledButtons();
	}

	private void defineMenu() {
		// Add new Category
		newFormButton = new IconButton(ThemeIcons.FORM_MANAGER_ADD_FORM.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().addWindow(new WindowNewForm(parent));
						parent.addForm();
					}
				});
		getMenuLayout().addComponent(newFormButton);
		getMenuLayout().setComponentAlignment(newFormButton, Alignment.MIDDLE_CENTER);

	}

	public void setEnabledButtons() {
		newFormButton.setEnabled(AbcdAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), DActivity.FORM_CREATE));
	}
}
