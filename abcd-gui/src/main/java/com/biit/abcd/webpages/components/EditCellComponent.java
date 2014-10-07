package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.elements.decisiontable.CellDoubleClickedListener;
import com.biit.form.TreeObject;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class EditCellComponent extends CustomComponent {
	private static final long serialVersionUID = -5757196311785954280L;
	private static final String CLASSNAME = "v-edit-cell-component";

	private CssLayout rootLayout;
	private IconButton editButton, removeButton;
	private Label textLabel;
	private boolean selectValue;
	private boolean onlyEdit = false;
	private List<CellDoubleClickedListener> doubleClickListeners;

	public EditCellComponent() {
		selectValue = false;
		doubleClickListeners = new ArrayList<>();

		addStyleName(CLASSNAME);

		rootLayout = new CssLayout();
		rootLayout.setWidth(null);
		rootLayout.setHeight(null);
		rootLayout.setImmediate(true);

		editButton = new IconButton(ThemeIcon.EDIT, IconSize.SMALL, LanguageCodes.EDIT_BUTTON_TOOLTIP);
		removeButton = new IconButton(ThemeIcon.DELETE, IconSize.SMALL, LanguageCodes.DELETE_BUTTON_TOOLTIP);
		textLabel = new Label(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE), ContentMode.HTML);
		textLabel.setSizeUndefined();

		rootLayout.addComponent(textLabel);
		rootLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = -5210194218425133869L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.isDoubleClick()) {
					doubleClickElement();
				}
			}
		});

		setCompositionRoot(rootLayout);
		setSizeUndefined();
	}

	protected void doubleClickElement() {
		for (CellDoubleClickedListener listener : doubleClickListeners) {
			listener.isDoubleClick();
		}
	}

	public void select(boolean value) {
		selectValue = value;
		if (value) {
			addButtons();
		} else {
			removeButtons();
		}
	}

	public void select() {
		select(!selectValue);
	}

	protected void addButtons() {
		if (editButton.getParent() == null) {
			if (!isOnlyEdit()) {
				rootLayout.addComponent(removeButton, 0);
			}
			rootLayout.addComponent(editButton, 0);
		}
	}

	protected void removeButtons() {
		if (editButton.getParent() != null) {
			rootLayout.removeComponent(editButton);
			rootLayout.removeComponent(removeButton);
		}
	}

	public void addLayoutClickListener(LayoutClickListener listener) {
		rootLayout.addLayoutClickListener(listener);
	}

	public void removeLayoutClickListener(LayoutClickListener listener) {
		rootLayout.removeLayoutClickListener(listener);
	}

	public void addEditButtonClickListener(ClickListener listener) {
		editButton.addClickListener(listener);
	}

	public void removeEditButtonClickListener(ClickListener listener) {
		editButton.removeClickListener(listener);
	}

	public void addRemoveButtonClickListener(ClickListener listener) {
		removeButton.addClickListener(listener);
	}

	public void removeRemoveButtonClickListener(ClickListener listener) {
		removeButton.removeClickListener(listener);
	}

	public void setLabel(String label) {
		textLabel.setValue(label);
	}

	public void setLabel(TreeObject treeObject) {
		if (treeObject == null) {
			setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
		} else {
			setLabel(treeObject.toString());
		}
	}

	/**
	 * Disables/Enables the delete button
	 * 
	 * @param onlyEdit
	 */
	public void setOnlyEdit(boolean onlyEdit) {
		this.onlyEdit = onlyEdit;
	}

	protected boolean isOnlyEdit() {
		return onlyEdit;
	}

	public void addDoubleClickListener(CellDoubleClickedListener listener) {
		doubleClickListeners.add(listener);
	}

	public void removeDoubleClickListener(CellDoubleClickedListener listener) {
		doubleClickListeners.remove(listener);
	}

	protected CssLayout getRootLayout() {
		return rootLayout;
	}

	protected IconButton getEditButton() {
		return editButton;
	}

	protected IconButton getRemoveButton() {
		return removeButton;
	}
}
