package com.biit.abcd.webpages.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

@Component
public class TableFormsCollapsible extends VerticalLayout implements ValueChangeNotifier {
	private static final long serialVersionUID = -5943739337345699263L;
	private static String COL_NAME = "Name";
	private static String COL_VERSION = "Version";
	private static String COL_ACCESS = "Access";
	private static String COL_USED_BY = "Used by";
	private static String COL_CREATED_BY = "Created by";
	private static String COL_CREATION_DATE = "Creation date";
	private static String COL_MODIFIED_BY = "Modified by";
	private static String COL_MODIFICATION_DATE = "Modified date";

	private HashMap<String, List<Form>> formMap;
	private Table formTable;
	private List<ValueChangeListener> valueChangeListeners;

	@Autowired
	private IFormDao formDao;

	public TableFormsCollapsible() {
		initUi();
		valueChangeListeners = new ArrayList<>();
	}

	private void initUi() {
		removeAllComponents();

		setMargin(true);
		setSpacing(true);
		setSizeFull();

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

		formTable.addContainerProperty(COL_NAME, CollapsibleCellLabel.class, null);
		formTable.addContainerProperty(COL_VERSION, StringLabel.class, null);
		formTable.addContainerProperty(COL_ACCESS, StringLabel.class, null);
		formTable.addContainerProperty(COL_USED_BY, StringLabel.class, null);
		formTable.addContainerProperty(COL_CREATED_BY, StringLabel.class, null);
		formTable.addContainerProperty(COL_CREATION_DATE, StringLabel.class, null);
		formTable.addContainerProperty(COL_MODIFIED_BY, StringLabel.class, null);
		formTable.addContainerProperty(COL_MODIFICATION_DATE, StringLabel.class, null);

		formTable.setColumnAlignment(COL_NAME, Align.LEFT);
		formTable.setColumnAlignment(COL_VERSION, Align.CENTER);
		formTable.setColumnAlignment(COL_ACCESS, Align.CENTER);
		formTable.setColumnAlignment(COL_USED_BY, Align.CENTER);
		formTable.setColumnAlignment(COL_CREATED_BY, Align.CENTER);
		formTable.setColumnAlignment(COL_CREATION_DATE, Align.CENTER);
		formTable.setColumnAlignment(COL_MODIFIED_BY, Align.CENTER);
		formTable.setColumnAlignment(COL_MODIFICATION_DATE, Align.CENTER);

		formTable.setColumnCollapsingAllowed(true);
		formTable.setColumnCollapsible(COL_NAME, false);
		formTable.setColumnCollapsible(COL_VERSION, false);
		formTable.setColumnCollapsible(COL_ACCESS, true);
		formTable.setColumnCollapsible(COL_USED_BY, true);
		formTable.setColumnCollapsible(COL_CREATED_BY, true);
		formTable.setColumnCollapsible(COL_CREATION_DATE, true);
		formTable.setColumnCollapsible(COL_MODIFIED_BY, true);
		formTable.setColumnCollapsible(COL_MODIFICATION_DATE, true);
		formTable.setColumnCollapsed(COL_CREATED_BY, true);
		formTable.setColumnCollapsed(COL_CREATION_DATE, true);
		formTable.setColumnCollapsed(COL_MODIFIED_BY, true);

		formTable.setColumnWidth(COL_VERSION, 45);
		formTable.setColumnWidth(COL_ACCESS, 55);
		formTable.setColumnWidth(COL_USED_BY, 65);
		formTable.setColumnWidth(COL_MODIFICATION_DATE, 130);

		formTable.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5598877051361847210L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() instanceof Form) {
					UserSelectedRow.getInstance().setSelected(UserSessionHandler.getUser(),
							(Form) event.getProperty().getValue());
					sendValueChangeEvent((Form) event.getProperty().getValue());
				} else {
					UserSelectedRow.getInstance().setSelected(UserSessionHandler.getUser(), null);
					sendValueChangeEvent(null);
				}
			}
		});
	}

	private void initializeFormTable() {
		formTable = new Table();
		// Table properties
		setTableProperties();

		formMap = initializeFormData();

		for (List<Form> forms : formMap.values()) {
			addRow(forms);
		}

		formTable.setSortContainerPropertyId("Name");
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
		formTable.addItem(new Object[] { new CollapsibleFormParent(forms), null, null, null, null, null, null, null,
				null, null }, forms);
	}

	@SuppressWarnings("unchecked")
	private void addChildRows(List<Form> forms) {
		if (forms.isEmpty()) {
			return;
		}
		for (Form form : forms) {
			Item newItem = formTable.addItemAfter(forms, form);
			if (newItem == null) {
				return;
			}
			newItem.getItemProperty(COL_NAME).setValue(new CollapsibleFormLeaf(form));
			newItem.getItemProperty(COL_VERSION).setValue(new StringLabel(form.getVersion().toString()));
			newItem.getItemProperty(COL_ACCESS).setValue(new StringLabel(getFormPermissionsTag(form)));
			newItem.getItemProperty(COL_USED_BY).setValue(new StringLabel(""));
			newItem.getItemProperty(COL_CREATED_BY).setValue(new StringLabel(form.getCreatedBy().getEmailAddress()));
			newItem.getItemProperty(COL_CREATION_DATE).setValue(
					new StringLabel(DateManager.convertDateToString(form.getCreationTime())));
			newItem.getItemProperty(COL_MODIFIED_BY).setValue(new StringLabel(form.getUpdatedBy().getEmailAddress()));
			newItem.getItemProperty(COL_MODIFICATION_DATE).setValue(
					new StringLabel(DateManager.convertDateToString(form.getUpdateTime())));
		}
	}

	private void removeChildRows(List<Form> forms) {
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
		Long selectedFormId = UserSelectedRow.getInstance().getSelectedFormId(UserSessionHandler.getUser());
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
			((CollapsibleFormParent) formTable.getContainerProperty(forms, COL_NAME).getValue()).uncollapse();
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

	private abstract class CollapsibleCellLabel extends HorizontalLayout implements Comparable<CollapsibleCellLabel> {
		private static final long serialVersionUID = -721622752018794079L;
	}

	private class CollapsibleFormLeaf extends CollapsibleCellLabel {
		private static final long serialVersionUID = 712210189357730840L;
		private Form form;
		private Label label;

		public CollapsibleFormLeaf(Form form) {
			this.form = form;
			initUi();
			addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = -8168062755223513913L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					formTable.setValue(getForm());
				}
			});
		}

		private Form getForm() {
			return form;
		}

		private void initUi() {
			setImmediate(true);
			setSpacing(false);
			setSizeUndefined();
			setMargin(new MarginInfo(false, false, false, true));

			if (form != null) {
				label = new Label(form.getName());
			} else {
				label = new Label("");
			}

			addComponent(label);
			setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		}

		@Override
		public int compareTo(CollapsibleCellLabel o) {
			if (o instanceof CollapsibleFormParent) {
				// It's a form list
				CollapsibleFormParent cell = (CollapsibleFormParent) o;
				if (cell.getName().equals(form.getName())) {
					// If they have the same name
					return 0;
				} else {
					return form.getName().compareTo(cell.getName());
				}
			} else {
				// It's a form
				CollapsibleFormLeaf cell = (CollapsibleFormLeaf) o;
				if (form.getName().equals(cell.getForm().getName())) {
					// if equals, then version number
					return form.getVersion().compareTo(cell.getForm().getVersion());
				} else {
					return form.getName().compareTo(cell.getForm().getName());
				}
			}
		}
	}

	private class CollapsibleFormParent extends CollapsibleCellLabel {
		private static final long serialVersionUID = 3002037698058869287L;
		private List<Form> forms;
		private Button uncollapsedButton;
		private Button collapsedButton;
		private Label label;

		public CollapsibleFormParent(List<Form> forms) {
			this.forms = forms;
			initUi();
			addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = -8168062755223513913L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					formTable.setValue(getForms());
				}
			});
		}

		public List<Form> getForms() {
			return forms;
		}

		public String getName() {

			String name;
			if (forms != null && forms.get(0) != null) {
				name = new String(forms.get(0).getName());
			} else {
				name = new String("");
			}
			return name;
		}

		private void initUi() {
			setImmediate(true);
			setSpacing(true);
			setSizeUndefined();

			collapsedButton = new Button("Expand forms", new ClickListener() {
				private static final long serialVersionUID = -8790931587642079896L;

				@Override
				public void buttonClick(ClickEvent event) {
					uncollapse();
				}
			});

			uncollapsedButton = new Button("Contract forms", new ClickListener() {
				private static final long serialVersionUID = 3722311031488330957L;

				@Override
				public void buttonClick(ClickEvent event) {
					collapse();
				}
			});

			if (forms != null && forms.get(0) != null) {
				label = new Label(forms.get(0).getName());
			} else {
				label = new Label("");
			}

			addComponent(collapsedButton);
			setComponentAlignment(collapsedButton, Alignment.MIDDLE_LEFT);
			addComponent(label);
			setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		}

		private void collapse() {
			removeAllComponents();
			addComponent(collapsedButton);
			setComponentAlignment(collapsedButton, Alignment.MIDDLE_LEFT);
			addComponent(label);
			setComponentAlignment(label, Alignment.MIDDLE_LEFT);
			markAsDirty();
			removeChildRows(forms);
		}

		private void uncollapse() {
			removeAllComponents();
			addComponent(uncollapsedButton);
			setComponentAlignment(uncollapsedButton, Alignment.MIDDLE_LEFT);
			addComponent(label);
			setComponentAlignment(label, Alignment.MIDDLE_LEFT);
			markAsDirty();
			addChildRows(forms);
		}

		public int compareTo(CollapsibleFormParent o) {
			String ori = "";
			String dest = "";

			if (forms != null && forms.get(0) != null) {
				ori = forms.get(0).getName();
			}
			if (o.forms != null && o.forms.get(0) != null) {
				dest = o.forms.get(0).getName();
			}

			return ori.compareTo(dest);
		}

		public int compareTo(CollapsibleFormLeaf o) {
			String ori = "";
			String dest = "";

			ori = getName();

			dest = o.getForm().getName();

			if (ori.equals(dest)) {
				return 0;
			} else {
				return ori.compareTo(dest);
			}
		}

		@Override
		public int compareTo(CollapsibleCellLabel o) {
			if (o instanceof CollapsibleFormParent) {
				return compareTo((CollapsibleFormParent) o);
			} else {
				return compareTo((CollapsibleFormLeaf) o);
			}
		}
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
}
