package com.biit.abcd.webpages;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formtable.FormsCollapsibleTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;

public class FormManager extends FormWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private FormsCollapsibleTable formTable;
	private FormManagerUpperMenu upperMenu;

	private IFormDao formDao;

	public FormManager() {
		super();
		updateButtons(false);

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		this.upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

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
				// updateButtons(getForm() != null
				// && AbcdAuthorizationService.getInstance().canEditForm(getForm(), UserSessionHandler.getUser(),
				// DActivity.FORM_EDITING));
				updateButtons(getForm() != null);
			}
		});
		return formTable;
	}

	private FormManagerUpperMenu createUpperMenu() {
		FormManagerUpperMenu upperMenu = new FormManagerUpperMenu(this);
		return upperMenu;
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
		;
	}

	public void addForm(Form form) {
		formTable.addNewForm(form);
		formDao.makePersistent(form);
	}

}
