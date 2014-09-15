package com.biit.abcd.webpages;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.elements.formdesigner.RootForm;
import com.biit.abcd.webpages.elements.formmanager.FormManagerUpperMenu;
import com.biit.abcd.webpages.elements.formtable.FormsVersionsTreeTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.VerticalLayout;

public class FormManager extends FormWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private FormsVersionsTreeTable formTable;
	private FormManagerUpperMenu upperMenu;

	private IFormDao formDao;

	public FormManager() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	protected void initContent() {
		this.upperMenu = createUpperMenu();
		upperMenu.addFormSelectedListener(new IFormSelectedListener() {

			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					UserSessionHandler.getFormController().setForm(formDao.read(formTable.getValue().getId()));
				}
			}
		});
		setUpperMenu(upperMenu);

		formTable = createTreeTable();
		VerticalLayout rootLayout = new VerticalLayout(formTable);
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		getWorkingAreaLayout().addComponent(rootLayout);
		formTable.selectLastUsedForm();
		updateButtons(!(getForm() instanceof RootForm) && getForm() != null);

		getBottomMenu().addFormSelectedListener(new IFormSelectedListener() {
			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					UserSessionHandler.getFormController().setForm(formDao.read(formTable.getValue().getId()));
				}
			}
		});
	}

	private FormsVersionsTreeTable createTreeTable() {
		FormsVersionsTreeTable treeTable = new FormsVersionsTreeTable();
		treeTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -119450082492122880L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateButtons(!(getForm() instanceof RootForm) && getForm() != null);
			}
		});
		return treeTable;
	}

	private FormManagerUpperMenu createUpperMenu() {
		FormManagerUpperMenu upperMenu = new FormManagerUpperMenu(this);
		return upperMenu;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return Arrays.asList(DActivity.READ);
	}

	public SimpleFormView getForm() {
		return formTable.getValue();
	}

	public void addNewForm(Form form) {
		SimpleFormView simpleView = new SimpleFormView(form);
		formTable.addForm(simpleView);
		formTable.selectForm(simpleView);
		formDao.makePersistent(form);
	}

}
