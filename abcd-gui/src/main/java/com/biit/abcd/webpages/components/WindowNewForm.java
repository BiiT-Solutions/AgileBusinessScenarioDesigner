package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.FormManager;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowNewForm extends Window {
	private static final long serialVersionUID = 2963807969133587359L;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 170;
	private Form form;
	private FormManager parent;

	private IFormDao formDao;

	public WindowNewForm(FormManager parent) {
		super();
		this.parent = parent;
		form = new Form();
		setWidth(Math.min(WINDOW_WIDTH, UI.getCurrent().getPage().getBrowserWindowWidth()), Unit.PIXELS);
		setHeight(Math.min(WINDOW_HEIGHT, UI.getCurrent().getPage().getBrowserWindowHeight()), Unit.PIXELS);
		this.setCaption(ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER));

		setContent(generateContent());
		setResizable(false);
		setModal(true);
		center();

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	public AbstractOrderedLayout generateContent() {
		VerticalLayout mainLayout = new VerticalLayout();

		final TextField formName = new TextField(ServerTranslate.tr(LanguageCodes.WINDOW_NEWFORM_NAME_TEXTFIELD));
		formName.setValue(form.getName());
		formName.setWidth("100%");
		// formDescription.setMaxLength(Form.MAX_DESCRIPTION_LENGTH);

		mainLayout.addComponent(formName);
		mainLayout.setExpandRatio(formName, 1.0f);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		Button save = new IconButton(LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_LABEL, ThemeIcons.ACCEPT,
				LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -3292565406584483547L;

					@Override
					public void buttonClick(ClickEvent event) {
						if (formDao.getForm(formName.getValue()) == null) {
							form.setName(formName.getValue());
							form.setCreatedBy(UserSessionHandler.getUser());
							form.setUpdatedBy(UserSessionHandler.getUser());
							parent.addForm(form);
							close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_FORM_NAME);
						}
					}
				});

		buttonLayout.addComponent(save);
		buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_CENTER);

		Button cancel = new IconButton(LanguageCodes.WINDOW_NEWFORM_CANCELBUTTON_LABEL, ThemeIcons.CANCEL, null,
				IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = 521904682248680077L;

					@Override
					public void buttonClick(ClickEvent event) {
						close();
					}
				});

		buttonLayout.addComponent(cancel);
		buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		return mainLayout;
	}

}
