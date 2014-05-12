package com.biit.abcd.webpages.elements.formtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.SpringContextHelper;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.utils.DateManager;
import com.biit.liferay.access.UserPool;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

public class FormsCollapsibleTable extends VerticalLayout implements ValueChangeNotifier {
	private static final long serialVersionUID = -5943739337345699263L;
	private String columnName = "Name";
	private String columnVersion = "Version";
	private String columnAccess = "Access";
	private String columnUsedBy = "Used by";
	private String columnCreatedBy = "Created by";
	private String columnCreationDate = "Creation date";
	private String columnModifiedBy = "Modified by";
	private String columnModicationDate = "Modification date";

	private HashMap<String, List<Form>> formMap;
	private Table formTable;
	private List<ValueChangeListener> valueChangeListeners;

	private IFormDao formDao;

	public FormsCollapsibleTable() {
		valueChangeListeners = new ArrayList<>();
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	private void setLanguage() {
		columnName = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_NAME);
		columnVersion = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_VERSION);
		columnAccess = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_ACCESS);
		columnUsedBy = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_USEDBY);
		columnCreatedBy = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY);
		columnCreationDate = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE);
		columnModifiedBy = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY);
		columnModicationDate = ServerTranslate.tr(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE);
	}

	/**
	 * InitTable cannot be used in the constructor due to Spring uses the constructor before Vaadin.
	 */
	public void initTable() {
		removeAllComponents();

		setMargin(true);
		setSpacing(true);
		setSizeFull();

		setLanguage();
		initializeFormTable();
		addComponent(formTable);
	}

	/**
	 * Sets the properties to the table.
	 * 
	 */
	private void setTableProperties() {
		formTable.setSelectable(true);
		formTable.setImmediate(true);
		formTable.setMultiSelect(false);
		formTable.setNullSelectionAllowed(false);
		formTable.setSizeFull();

		formTable.addContainerProperty(FormsTableColumns.NAME, CollapsibleCellLabel.class, null, columnName, null,
				Align.LEFT);
		formTable.addContainerProperty(FormsTableColumns.VERSION, StringLabel.class, null, columnVersion, null,
				Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.ACCESS, StringLabel.class, null, columnAccess, null,
				Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.USED_BY, StringLabel.class, null, columnUsedBy, null,
				Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.CREATED_BY, StringLabel.class, null, columnCreatedBy, null,
				Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.CREATION_DATE, StringLabel.class, null, columnCreationDate,
				null, Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.MODIFIED_BY, StringLabel.class, null, columnModifiedBy, null,
				Align.CENTER);
		formTable.addContainerProperty(FormsTableColumns.MODIFICATION_DATE, StringLabel.class, null,
				columnModicationDate, null, Align.CENTER);

		formTable.setColumnCollapsingAllowed(true);
		formTable.setColumnCollapsible(FormsTableColumns.NAME, false);
		formTable.setColumnCollapsible(FormsTableColumns.VERSION, false);
		formTable.setColumnCollapsible(FormsTableColumns.ACCESS, true);
		formTable.setColumnCollapsible(FormsTableColumns.USED_BY, true);
		formTable.setColumnCollapsible(FormsTableColumns.CREATED_BY, true);
		formTable.setColumnCollapsible(FormsTableColumns.CREATION_DATE, true);
		formTable.setColumnCollapsible(FormsTableColumns.MODIFIED_BY, true);
		formTable.setColumnCollapsible(FormsTableColumns.MODIFICATION_DATE, true);
		formTable.setColumnCollapsed(FormsTableColumns.CREATED_BY, true);
		formTable.setColumnCollapsed(FormsTableColumns.CREATION_DATE, true);

		formTable.setColumnExpandRatio(FormsTableColumns.NAME, 3);
		formTable.setColumnExpandRatio(FormsTableColumns.VERSION, (float) 0.5);
		formTable.setColumnExpandRatio(FormsTableColumns.ACCESS, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.USED_BY, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.CREATED_BY, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.CREATION_DATE, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.MODIFIED_BY, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.MODIFICATION_DATE, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.CREATED_BY, 1);
		formTable.setColumnExpandRatio(FormsTableColumns.CREATION_DATE, 1);

		formTable.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5598877051361847210L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() instanceof Form) {
					UserSelectedTableRow.getInstance().setSelected(UserSessionHandler.getUser(),
							(Form) event.getProperty().getValue());
					sendValueChangeEvent((Form) event.getProperty().getValue());
				} else {
					UserSelectedTableRow.getInstance().setSelected(UserSessionHandler.getUser(), null);
					sendValueChangeEvent(null);
				}
			}
		});
	}

	public void addNewForm(Form form) {
		List<Form> listFormsForName = new ArrayList<Form>();
		listFormsForName.add(form);
		formMap.put(form.getName(), listFormsForName);
		addRow(listFormsForName);
		formTable.sort();
		selectForm(form);
	}

	private void initializeFormTable() {
		formTable = new Table();
		// Table properties
		setTableProperties();

		formMap = initializeFormData();

		for (List<Form> forms : formMap.values()) {
			addRow(forms);
		}

		formTable.setSortContainerPropertyId(FormsTableColumns.NAME);
		formTable.setSortAscending(true);
		formTable.sort();
	}

	/**
	 * This function adds a row to the table only if the list of forms is not empty.
	 * 
	 * @param forms
	 */
	private void addRow(List<Form> forms) {
		if (forms.isEmpty()) {
			return;
		}
		formTable.addItem(
				new Object[] { new CollapsibleCellRoot(this, forms), null, null, null, null, null, null, null }, forms);
	}

	@SuppressWarnings("unchecked")
	void addChildRows(List<Form> forms) {
		if (forms.isEmpty()) {
			return;
		}
		for (Form form : forms) {
			Item newItem = formTable.addItemAfter(forms, form);
			if (newItem == null) {
				return;
			}
			newItem.getItemProperty(FormsTableColumns.NAME).setValue(new CollapsibleCellLeaf(this, form));
			newItem.getItemProperty(FormsTableColumns.VERSION).setValue(new StringLabel(form.getVersion().toString()));
			newItem.getItemProperty(FormsTableColumns.ACCESS).setValue(new StringLabel(getFormPermissionsTag(form)));
			newItem.getItemProperty(FormsTableColumns.USED_BY).setValue(new StringLabel(""));
			if (form.getCreatedBy() != null) {
				if (form.getCreatedBy() != null && UserPool.getInstance().getUserById(form.getCreatedBy()) != null) {
					newItem.getItemProperty(FormsTableColumns.CREATED_BY).setValue(
							new StringLabel(UserPool.getInstance().getUserById(form.getCreatedBy()).getEmailAddress()));
				}
			}
			if (form.getCreationTime() != null) {
				newItem.getItemProperty(FormsTableColumns.CREATION_DATE).setValue(
						new StringLabel(DateManager.convertDateToString(form.getCreationTime())));
			}
			if (form.getUpdatedBy() != null) {
				if (form.getCreatedBy() != null && UserPool.getInstance().getUserById(form.getUpdatedBy()) != null) {
					newItem.getItemProperty(FormsTableColumns.MODIFIED_BY).setValue(
							new StringLabel(UserPool.getInstance().getUserById(form.getUpdatedBy()).getEmailAddress()));
				}
			}
			if (form.getUpdateTime() != null) {
				newItem.getItemProperty(FormsTableColumns.MODIFICATION_DATE).setValue(
						new StringLabel(DateManager.convertDateToString(form.getUpdateTime())));
			}
		}
	}

	void removeChildRows(List<Form> forms) {
		for (Form form : forms) {
			formTable.removeItem(form);
		}
	}

	/**
	 * This function returs an string with read only if the form can't be edited by the user
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
		Long selectedFormId = UserSelectedTableRow.getInstance().getSelectedFormId(UserSessionHandler.getUser());
		if (selectedFormId != null) {
			// Update form with new object if the form has change.
			Form form = formDao.read(selectedFormId);
			selectForm(form);
		} else {
			// Select default one.
			selectFirstRow();
		}
	}

	/**
	 * This function uncollapses a form if exists but it's not already uncollapsed.
	 */
	private void uncollapseForm(Form form) {
		if (formMap.containsKey(form)) {
			// It's already in the table, return.
			return;
		}
		List<Form> forms = formMap.get(form.getName());
		if (forms != null) {
			((CollapsibleCellRoot) formTable.getContainerProperty(forms, FormsTableColumns.NAME).getValue())
					.uncollapse();
		}
	}

	/**
	 * This function selects a form from the table.
	 */
	private void selectForm(Form form) {
		if (form != null && !formTable.containsId(form)) {
			uncollapseForm(form);
		}
		formTable.setValue(form);
	}

	/**
	 * Selects the first row.
	 */
	private void selectFirstRow() {
		formTable.setValue(formTable.firstItemId());
	}

	public Form getValue() {
		if (formTable.getValue() instanceof Form) {
			return (Form) formTable.getValue();
		}
		return null;
	}

	public int getNumberOfRows() {
		return formTable.getItemIds().size();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		valueChangeListeners.add(listener);
	}

	@Override
	public void removeValueChangeListener(ValueChangeListener listener) {
		valueChangeListeners.remove(listener);
	}

	@Override
	@Deprecated
	public void addListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	@Deprecated
	public void removeListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
	}

	private void sendValueChangeEvent(final Form form) {
		for (ValueChangeListener listener : valueChangeListeners) {
			listener.valueChange(new ValueChangeEvent() {
				private static final long serialVersionUID = -222412923128363030L;

				@SuppressWarnings("rawtypes")
				@Override
				public Property getProperty() {
					return new ObjectProperty<Form>(form);
				}
			});
		}
	}

	private class StringLabel extends HorizontalLayout {

		private static final long serialVersionUID = -8567825700201237924L;

		public StringLabel(String label) {
			Label textLabel = new Label(label);
			addComponent(textLabel);
			setComponentAlignment(textLabel, Alignment.MIDDLE_LEFT);
		}
	}

	public Table getFormTable() {
		return formTable;
	}
}
