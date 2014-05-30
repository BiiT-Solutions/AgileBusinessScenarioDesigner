package com.biit.abcd.webpages;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formtable.FormsVersionsTreeTable;
import com.biit.abcd.webpages.elements.formtable.UserSelectedTableRow;
import com.biit.abcd.webpages.elements.treetable.RootForm;
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
		setUpperMenu(upperMenu);

		formTable = createTreeTable();
		VerticalLayout rootLayout = new VerticalLayout(formTable);
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		formTable.selectLastUsedForm();
		updateButtons(!(getForm() instanceof RootForm) && getForm() != null);
		getWorkingAreaLayout().addComponent(rootLayout);
	}

	private FormsVersionsTreeTable createTreeTable() {
		FormsVersionsTreeTable treeTable = new FormsVersionsTreeTable();
		treeTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -119450082492122880L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// updateButtons(getForm() != null
				// &&
				// AbcdAuthorizationService.getInstance().canEditForm(getForm(),
				// UserSessionHandler.getUser(),
				// DActivity.FORM_EDITING));
				if (!(getForm() instanceof RootForm)) {
					UserSelectedTableRow.getInstance().setSelected(UserSessionHandler.getUser(), getForm());
				}
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

	@Override
	public Form getForm() {
		return formTable.getValue();
	}

	@Override
	public void setForm(Form form) {
		;
	}

	public void addForm(Form form) {
		formTable.addForm(form);
		formDao.makePersistent(form);
	}

}
