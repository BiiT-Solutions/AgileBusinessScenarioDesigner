package com.biit.abcd.webpages;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formtable.FormsCollapsibleTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;

public class FormManager extends FormWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private FormsCollapsibleTable formTable;
	private Form form;

	public FormManager() {
		super();
		updateButtons(false);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		setAsOneWindowWithBottomMenu();
		formTable = createTable();
		getWorkingAreaLayout().addComponent(formTable);
		getWorkingAreaLayout().setComponentAlignment(formTable, Alignment.MIDDLE_CENTER);
		formTable.selectLastUsedForm();
	}

	private FormsCollapsibleTable createTable() {
		FormsCollapsibleTable formTable = new FormsCollapsibleTable();
		formTable.initTable();
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -119450082492122880L;

			@Override
			public void valueChange(ValueChangeEvent event) {
//				updateButtons(getForm() != null
//						&& AbcdAuthorizationService.getInstance().canEditForm(getForm(), UserSessionHandler.getUser(),
//								DActivity.FORM_EDITING));
				updateButtons(getForm() != null);
			}
		});
		return formTable;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return Arrays.asList(DActivity.READ);
	}

	@Override
	public Form getForm() {
		return formTable.getValue();
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

}
