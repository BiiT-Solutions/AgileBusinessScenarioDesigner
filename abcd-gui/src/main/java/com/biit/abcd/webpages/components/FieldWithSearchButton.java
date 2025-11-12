package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class FieldWithSearchButton extends CustomComponent {

	private static final long serialVersionUID = 233238112594951817L;

	private HorizontalLayout rootLayout;
	private IconOnlyButton searchButton;
	private IconOnlyButton removeButton;
	private TextField textField;
	private String nullCaption;
	private Object value;

	public FieldWithSearchButton() {
		super();
		initializeComponent(null);
	}

	public FieldWithSearchButton(String caption) {
		super();
		initializeComponent(caption);
	}

	private void initializeComponent(String caption) {
		if (caption != null) {
			setCaption(caption);
		}

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();

		searchButton = new IconOnlyButton(ThemeIcon.SEARCH.getThemeResource());
		searchButton.setWidth("20px");

		removeButton = new IconOnlyButton(ThemeIcon.CANCEL.getThemeResource());
		removeButton.addClickListener(new DeleteContentListener());
		removeButton.setWidth("20px");

		textField = new TextField();
		textField.setWidth("100%");
		textField.setValue(nullCaption);
		textField.setEnabled(false);
		textField.addStyleName("disabled-black");

		rootLayout.addComponent(textField);
		rootLayout.addComponent(searchButton);
		rootLayout.addComponent(removeButton);
		rootLayout.setExpandRatio(textField, 1.0f);
		rootLayout.setExpandRatio(searchButton, 0.0f);
		rootLayout.setExpandRatio(removeButton, 0.0f);

		setCompositionRoot(rootLayout);
	}

	public void setValue(Object value) {
		setValue(value, null);
	}

	public void setValue(Object value, String caption) {
		this.value = value;
		if (caption == null) {
			if (value != null) {
				textField.setValue(value.toString());
			} else {
				textField.setValue(nullCaption);
			}
		} else {
			textField.setValue(caption);
		}
	}

	public Object getValue() {
		return value;
	}

	public void setNullCaption(String nullCaption) {
		this.nullCaption = nullCaption;
		if (value == null) {
			textField.setValue(nullCaption);
		}
	}

	public void addClickListener(ClickListener listener) {
		searchButton.addClickListener(listener);
	}

	public void removeClickListener(ClickListener listener) {
		searchButton.removeClickListener(listener);
	}

	public void addRemoveClickListener(ClickListener listener) {
		removeButton.addClickListener(listener);
	}

	public void removeRemoveClickListener(ClickListener listener) {
		removeButton.removeClickListener(listener);
	}

	/**
	 * This class implements the delete content listener. It deletes the content of the field and sets the value to null
	 * when the user presses the delete button.
	 *
	 */
	private class DeleteContentListener implements ClickListener {

		private static final long serialVersionUID = 9126407065442331140L;

		@Override
		public void buttonClick(ClickEvent event) {
			setValue(null);
		}
	}

	public void setVisibleSearchButton(boolean visible) {
		searchButton.setVisible(visible);
	}

	public void setVisibleDeleteButton(boolean visible) {
		removeButton.setVisible(visible);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textField.setEnabled(enabled);
		searchButton.setEnabled(enabled);
		removeButton.setEnabled(enabled);
	}
}
