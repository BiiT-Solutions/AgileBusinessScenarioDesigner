package com.biit.abcd.webpages.elements.formtable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.utils.DateManager;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.TreeObjectTableCellStyleGenerator;
import com.biit.abcd.webpages.elements.formdesigner.RootForm;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TreeTable;

public class FormsVersionsTreeTable extends TreeTable {
	private static final long serialVersionUID = -7776688515497328826L;
	private ISimpleFormViewDao simpleFormViewDao;
	private HashMap<String, List<SimpleFormView>> formMap;

	enum FormsVersionsTreeTableProperties {
		FORM_LABEL, VERSION, ACCESS, GROUP, AVAILABLE_FROM, AVAILABLE_TO, USED_BY, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	public FormsVersionsTreeTable() {
		// Add Vaadin context to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		simpleFormViewDao = (ISimpleFormViewDao) helper.getBean("simpleFormViewDao");

		initContainerProperties();
		initializeFormTable();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(false);
		setSizeFull();

		addContainerProperty(FormsVersionsTreeTableProperties.FORM_LABEL, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormsVersionsTreeTableProperties.VERSION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_VERSION), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.GROUP, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_GROUP), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.ACCESS, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.USED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.AVAILABLE_FROM, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_AVAILABLEFROM), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_AVAILABLETO), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.CREATED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.CREATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.MODIFIED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY), null, Align.CENTER);

		addContainerProperty(FormsVersionsTreeTableProperties.MODIFICATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE), null, Align.CENTER);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(FormsVersionsTreeTableProperties.FORM_LABEL, false);
		setColumnCollapsible(FormsVersionsTreeTableProperties.VERSION, false);
		setColumnCollapsible(FormsVersionsTreeTableProperties.GROUP, true);
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

		setColumnExpandRatio(FormsVersionsTreeTableProperties.FORM_LABEL, 3);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.VERSION, 0.5f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.GROUP, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.ACCESS, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.AVAILABLE_FROM, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.AVAILABLE_TO, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.USED_BY, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.CREATION_DATE, 1);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(FormsVersionsTreeTableProperties.MODIFICATION_DATE, 1);

		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	/**
	 * This function adds a row to the table only if the list of forms is not empty.
	 * 
	 * @param forms
	 */
	private void addRow(SimpleFormView form) {
		if (form != null) {
			Item item = addItem(form);
			setRow(form, item);
		}
	}

	private void refreshRow(SimpleFormView form) {
		if (form != null) {
			Item item = getItem(form);
			setRow(form, item);
		}
	}

	@SuppressWarnings("unchecked")
	private void setRow(SimpleFormView form, Item item) {
		if (item != null) {
			item.getItemProperty(FormsVersionsTreeTableProperties.FORM_LABEL).setValue(form.getLabel());
			item.getItemProperty(FormsVersionsTreeTableProperties.VERSION).setValue(form.getVersion() + "");
			item.getItemProperty(FormsVersionsTreeTableProperties.ACCESS).setValue(getFormPermissionsTag(form));

			Organization organization;
			try {
				organization = AbcdFormAuthorizationService.getInstance().getOrganization(form.getOrganizationId());
				if (organization != null) {
					item.getItemProperty(FormsVersionsTreeTableProperties.GROUP).setValue(organization.getName());
				}
			} catch (IOException | AuthenticationRequired e1) {
				AbcdLogger.errorMessage(this.getClass().getName(), e1);
			}

			item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_FROM).setValue(
					(DateManager.convertDateToString(form.getAvailableFrom())));
			if (form.getAvailableTo() != null) {
				item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO).setValue(
						(DateManager.convertDateToString(form.getAvailableTo())));
			} else {
				item.getItemProperty(FormsVersionsTreeTableProperties.AVAILABLE_TO).setValue("");
			}
			User userAccessingForm = UiAccesser.getUserUsingForm(form.getId());
			if (userAccessingForm != null) {
				item.getItemProperty(FormsVersionsTreeTableProperties.USED_BY).setValue(
						userAccessingForm.getEmailAddress());
			} else {
				item.getItemProperty(FormsVersionsTreeTableProperties.USED_BY).setValue("");
			}
			try {
				item.getItemProperty(FormsVersionsTreeTableProperties.CREATED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getCreatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException | NullPointerException e) {
				item.getItemProperty(FormsVersionsTreeTableProperties.CREATED_BY).setValue("");
			}
			item.getItemProperty(FormsVersionsTreeTableProperties.CREATION_DATE).setValue(
					(DateManager.convertDateToStringWithHours(form.getCreationTime())));
			try {
				item.getItemProperty(FormsVersionsTreeTableProperties.MODIFIED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getUpdatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException | NullPointerException e) {
				item.getItemProperty(FormsVersionsTreeTableProperties.MODIFIED_BY).setValue("");
			}
			item.getItemProperty(FormsVersionsTreeTableProperties.MODIFICATION_DATE).setValue(
					(DateManager.convertDateToStringWithHours(form.getUpdateTime())));
		}
	}

	public void refreshSelectedRow() {
		SimpleFormView selectedForm = getValue();
		if (selectedForm != null && !(selectedForm instanceof RootForm)) {
			refreshRow(selectedForm);
		}
	}

	@SuppressWarnings("unchecked")
	private void addRow(RootForm form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(FormsVersionsTreeTableProperties.FORM_LABEL).setValue(form.getLabel());
		}
	}

	public void addForm(SimpleFormView form) {
		RootForm parent = getFormRoot(form);
		if (parent == null) {
			parent = new RootForm(form.getLabel(), form.getOrganizationId());
			// Needed to look form the children forms in the launch test window
			parent.setName(form.getName());
			addRow(parent);
		}
		if (form != null) {
			parent.addChildForm(form);
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
	private RootForm getFormRoot(SimpleFormView form) {
		for (Object item : getItemIds()) {
			if (item instanceof RootForm) {
				if ((form != null) && (((RootForm) item).getLabel() != null)) {
					if (((RootForm) item).getLabel().equals(form.getLabel())
							&& ((RootForm) item).getOrganizationId().equals(form.getOrganizationId())) {
						return (RootForm) item;
					}
				}
			}
		}
		return null;
	}

	public void refreshFormTable() {
		initializeFormTable();
	}

	private void initializeFormTable() {
		formMap = initializeFormData();
		removeAllItems();

		Set<Organization> userOrganizations = AbcdFormAuthorizationService.getInstance()
				.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), AbcdActivity.READ);

		// Add form if has enough permissions.
		for (List<SimpleFormView> forms : formMap.values()) {
			for (SimpleFormView form : forms) {
				for (Organization organization : userOrganizations) {
					if (form.getOrganizationId().equals(organization.getOrganizationId())) {
						addForm(form);
					}
				}
			}
		}

		setSortContainerPropertyId(FormsVersionsTreeTableProperties.FORM_LABEL);
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
	private HashMap<String, List<SimpleFormView>> initializeFormData() {
		HashMap<String, List<SimpleFormView>> formData = new HashMap<>();
		List<SimpleFormView> forms = new ArrayList<>();

		forms = simpleFormViewDao.getAll();
		for (SimpleFormView form : forms) {
			if (!formData.containsKey(form.getLabel())) {
				// First form with this name
				List<SimpleFormView> listFormsForLabel = new ArrayList<>();
				listFormsForLabel.add(form);
				formData.put(form.getLabel(), listFormsForLabel);
			} else {
				formData.get(form.getLabel()).add(form);
			}
		}

		return formData;
	}

	/**
	 * This function selects the last form used by the user or the first.
	 */
	public void selectLastUsedForm() {
		try {
			if (UserSessionHandler.getFormController().getForm() != null) {
				// Update form with new object if the form has change.
				selectForm(new SimpleFormView(UserSessionHandler.getFormController().getForm()));
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
	public void selectForm(SimpleFormView form) {
		if (form != null && !containsId(form)) {
			// uncollapseForm(form);
			setCollapsed(form, false);
		}
		for (Object itemId : getItemIds()) {
			if (itemId instanceof SimpleFormView) {
				SimpleFormView tableForm = (SimpleFormView) itemId;
				if (tableForm.getId() != null && tableForm.getId().equals(form.getId())) {
					setValue(tableForm);
				}
			}
		}
	}

	public void selectForm(Form form) {
		if (form != null) {
			for (Object itemId : getItemIds()) {
				if (itemId instanceof SimpleFormView) {
					SimpleFormView tableForm = (SimpleFormView) itemId;
					if (tableForm.getId() != null && tableForm.getId().equals(form.getId())) {
						setValue(tableForm);
					}
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
	public SimpleFormView getValue() {
		if (super.getValue() instanceof SimpleFormView) {
			return (SimpleFormView) super.getValue();
		}
		return null;
	}

	/**
	 * This function returns an string with read only if the form can't be edited by the user
	 * 
	 * @param form
	 * @return
	 */
	private String getFormPermissionsTag(SimpleFormView form) {
		if (AbcdFormAuthorizationService.getInstance().isFormReadOnly(form, UserSessionHandler.getUser())) {
			return "read only";
		}
		if (AbcdFormAuthorizationService.getInstance().isFormAlreadyInUse(form.getId(), UserSessionHandler.getUser())) {
			return "in use";
		}
		return "";
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return new ArrayList<>(Arrays.asList(FormsVersionsTreeTableProperties.FORM_LABEL));
	}

	public RootForm getSelectedRootForm() {
		if (getValue() instanceof RootForm) {
			return (RootForm) getValue();
		} else {
			return (RootForm) getParent(getValue());
		}
	}

}
