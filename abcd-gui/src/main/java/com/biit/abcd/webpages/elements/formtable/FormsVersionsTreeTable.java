package com.biit.abcd.webpages.elements.formtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.utils.DateManager;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormTreeTableCellStyleGenerator;
import com.biit.abcd.webpages.elements.treetable.RootForm;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TreeTable;

public class FormsVersionsTreeTable extends TreeTable {
	private static final long serialVersionUID = -7776688515497328826L;
	private IFormDao formDao;
	private HashMap<String, List<Form>> formMap;

	enum FormsVersionsTreeTableProperties {
		FORM_NAME, VERSION, ACCESS, AVAILABLE_FROM, AVAILABLE_TO, USED_BY, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	public FormsVersionsTreeTable() {
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");

		initContainerProperties();
		initializeFormTable();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(true);
		setSizeFull();

		addContainerProperty(FormsVersionsTreeTableProperties.FORM_NAME, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormsVersionsTreeTableProperties.VERSION, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_VERSION), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.ACCESS, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.AVAILABLE_FROM, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_AVAILABLEFROM), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_AVAILABLETO), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.USED_BY, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.CREATED_BY, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.CREATION_DATE, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.MODIFIED_BY, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.MODIFICATION_DATE, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE), null, Align.CENTER);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.FORM_NAME, false);
		setColumnCollapsible(FormsVersionsTreeTableProperties.VERSION, false);
		setColumnCollapsible(FormsVersionsTreeTableProperties.ACCESS, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.AVAILABLE_FROM, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.AVAILABLE_TO, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.USED_BY, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.CREATED_BY, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.CREATION_DATE, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.MODIFIED_BY, true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.MODIFICATION_DATE, true);
		setColumnCollapsed(FormsVersionsTreeTableProperties.CREATED_BY, true);
		setColumnCollapsed(FormsVersionsTreeTableProperties.CREATION_DATE, true);

		setColumnExpandRatio(FormsVersionsTreeTableProperties.FORM_NAME, 3);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.VERSION, 0.5f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.ACCESS, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.AVAILABLE_FROM, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.AVAILABLE_TO, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.USED_BY, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.CREATION_DATE, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.MODIFICATION_DATE, 1);

		setCellStyleGenerator(new FormTreeTableCellStyleGenerator());
	}

	/**
	 * This function adds a row to the table only if the list of forms is not empty.
	 * 
	 * @param forms
	 */
	@SuppressWarnings("unchecked")
	private void addRow(Form form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(FormsVersionsTreeTableProperties.FORM_NAME).setValue(form.getName());
			item.getItemProperty(FormsVersionsTreeTableProperties.VERSION).setValue(form.getVersion() + "");
			item.getItemProperty(FormsVersionsTreeTableProperties.ACCESS).setValue(getFormPermissionsTag(form));
			item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_FROM).setValue(
					(DateManager.convertDateToString(form.getAvailableFrom())));
			if (form.getAvailableTo() != null) {
				item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO).setValue(
						(DateManager.convertDateToString(form.getAvailableTo())));
			} else {
				item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO).setValue("");
			}
			item.getItemProperty(FormsVersionsTreeTableProperties.USED_BY).setValue("");
			try {
				item.getItemProperty(FormsVersionsTreeTableProperties.CREATED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getCreatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(FormsVersionsTreeTableProperties.CREATED_BY).setValue("");
			}
			item.getItemProperty(FormsVersionsTreeTableProperties.CREATION_DATE).setValue(
					(DateManager.convertDateToString(form.getCreationTime())));
			try {
				item.getItemProperty(FormsVersionsTreeTableProperties.MODIFIED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getUpdatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(FormsVersionsTreeTableProperties.MODIFIED_BY).setValue("");
			}
			item.getItemProperty(FormsVersionsTreeTableProperties.MODIFICATION_DATE).setValue(
					(DateManager.convertDateToString(form.getUpdateTime())));
		}
	}

	@SuppressWarnings("unchecked")
	private void addRow(RootForm form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(FormsVersionsTreeTableProperties.FORM_NAME).setValue(form.getName());
		}
	}

	public void addForm(Form form) {
		RootForm parent = getFormRoot(form);
		if (parent == null) {
			parent = new RootForm(form.getName());
			addRow(parent);
		}
		if (form != null) {
			addRow(form);
			setChildrenAllowed(parent, true);
			setParent(form, parent);
			setCollapsed(parent, false);
			setValue(form);
			setChildrenAllowed(form, false);
		}
		sort();
	}

	/**
	 * Gets the Form Root of a form if exists.
	 * 
	 * @param form
	 * @return
	 */
	private RootForm getFormRoot(Form form) {
		for (Object item : getItemIds()) {
			if (item instanceof RootForm) {
				if (((RootForm) item).getName().equals(form.getName())) {
					return (RootForm) item;
				}
			}
		}
		return null;
	}

	private void initializeFormTable() {
		formMap = initializeFormData();
		for (List<Form> forms : formMap.values()) {
			for (Form form : forms) {
				addForm(form);
			}
		}

		setSortContainerPropertyId(FormsVersionsTreeTableProperties.FORM_NAME);
		setSortAscending(true);
		sort();
	}

	/**
	 * This function loads from database all form elements and groups them by name. At the end it orders each form list
	 * by version number.
	 * 
	 * @return
	 * @throws NotConnectedToDatabaseException
	 */
	private HashMap<String, List<Form>> initializeFormData() {
		HashMap<String, List<Form>> formData = new HashMap<>();
		List<Form> forms = new ArrayList<>();

		forms = formDao.getAll();
		for (Form form : forms) {
			if (!formData.containsKey(form.getName())) {
				// First form with this name
				List<Form> listFormsForName = new ArrayList<Form>();
				listFormsForName.add(form);
				formData.put(form.getName(), listFormsForName);
			} else {
				formData.get(form.getName()).add(form);
			}
		}

		for (List<Form> formList : formData.values()) {
			Collections.sort(formList, new FormVersionComparator());
		}

		return formData;
	}

	/**
	 * This is a form comparator that sorts by version number. It is used to sort the lists of forms that we have
	 * created for each different form name.
	 * 
	 */
	private class FormVersionComparator implements Comparator<Form> {
		@Override
		public int compare(Form arg0, Form arg1) {
			return arg0.getVersion().compareTo(arg1.getVersion());
		}
	}

	/**
	 * This function selects the last form used by the user or the first.
	 */
	public void selectLastUsedForm() {
		try {
			if (UserSessionHandler.getFormController().getForm() != null) {
				// Update form with new object if the form has change.
				selectForm(UserSessionHandler.getFormController().getForm());
			} else {
				// Select default one.
				selectFirstRow();
			}
		} catch (Exception e) {
			// Select default one.
			selectFirstRow();
		}
	}

	/**
	 * This function selects a form from the table.
	 */
	public void selectForm(Form form) {
		if (form != null && !containsId(form)) {
			// uncollapseForm(form);
			setCollapsed(form, false);
		}
		for (Object itemId : getItemIds()) {
			if (itemId instanceof Form) {
				Form tableForm = (Form) itemId;
				if (tableForm.getId() != null && tableForm.getId().equals(form.getId())) {
					setValue(tableForm);
				}
			}
		}
	}

	/**
	 * Selects the first row.
	 */
	private void selectFirstRow() {
		setValue(firstItemId());
	}

	@Override
	public Form getValue() {
		if (super.getValue() instanceof Form) {
			return (Form) super.getValue();
		}
		return null;
	}

	/**
	 * This function returns an string with read only if the form can't be edited by the user
	 * 
	 * @param form
	 * @return
	 */
	private String getFormPermissionsTag(Form form) {
		String permissions = "";
		if (!AbcdAuthorizationService.getInstance().canEditForm(form, UserSessionHandler.getUser(),
				DActivity.FORM_EDITING)) {
			permissions = "read only";
		}
		return permissions;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return new ArrayList<>(Arrays.asList(FormsVersionsTreeTableProperties.FORM_NAME));
	}

}
