package com.biit.abcd.webpages;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formvariables.FormVariablesUpperMenu;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormVariables extends FormWebPageComponent {
	private static final long serialVersionUID = 8796076485600899730L;
	private FormVariablesUpperMenu upperMenu;
	private Form form;

	@Override
	protected void initContent() {
		this.upperMenu = initUpperMenu();
		setUpperMenu(upperMenu);

	}

	private FormVariablesUpperMenu initUpperMenu() {
		FormVariablesUpperMenu upperMenu = new FormVariablesUpperMenu();

		upperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7788465178005102302L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		return upperMenu;
	}

	private void save() {
		if (form != null) {
			try {
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (ConstraintViolationException cve) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY_CAPTION);
			}
		}
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		if (form != null) {

		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

}
