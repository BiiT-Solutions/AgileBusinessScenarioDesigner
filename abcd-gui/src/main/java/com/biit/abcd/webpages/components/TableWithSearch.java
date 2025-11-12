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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Container.Filterable;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

/**
 * Custom component that takes a table and a filter with IFilterContainsText as interface and creates a visualization
 * that filters the table when the input in the search field changes.
 *
 *
 */
public class TableWithSearch extends CustomComponent {
	private static final long serialVersionUID = 8514074241552385601L;
	private static final String FULL = "100%";

	private final Table table;
	private final IFilterContainsText filterContainText;
	private VerticalLayout rootLayout;
	private TextField searchField;
	private int lastTextSize = 0;

	public TableWithSearch(Table table, IFilterContainsText filterContainText) {
		this.table = table;
		this.filterContainText = filterContainText;
		setCompositionRoot(generateContent());
	}

	public void setSearchCaptionAtLeft(boolean value) {
		configureRootLayout(true);
	}

	private void configureRootLayout(boolean captionAtLeft) {
		rootLayout.removeAllComponents();

		searchField = new TextField();
		searchField.setWidth(FULL);
		searchField.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 5165894413852789563L;

			@Override
			public void textChange(TextChangeEvent event) {
				String text = event.getText();
				Filterable container = (Filterable) table.getContainerDataSource();
				container.removeAllContainerFilters();
				filterContainText.setFilterText(text);
				container.addContainerFilter(filterContainText);
				if (text.length() > 0 && lastTextSize == 0) {
					if (table instanceof TreeTable) {
						lastTextSize = text.length();
						for (Object row : table.getItemIds()) {
							((TreeTable) table).setCollapsed(row, false);
						}
					}
				}
			}
		});

		if (captionAtLeft) {
			HorizontalLayout fieldWithCaptionAtLeft = new HorizontalLayout();
			fieldWithCaptionAtLeft.setWidth(FULL);
			fieldWithCaptionAtLeft.setSpacing(true);

			Label captionLabel = new Label(ServerTranslate.translate(LanguageCodes.CAPTION_SEARCH));
			captionLabel.setWidth(null);

			fieldWithCaptionAtLeft.addComponent(captionLabel);
			fieldWithCaptionAtLeft.addComponent(searchField);
			fieldWithCaptionAtLeft.setExpandRatio(searchField, 1.0f);

			rootLayout.addComponent(fieldWithCaptionAtLeft);
		} else {
			searchField.setCaption(ServerTranslate.translate(LanguageCodes.CAPTION_SEARCH));
			rootLayout.addComponent(searchField);
		}

		table.setSizeFull();
		rootLayout.addComponent(table);

		rootLayout.setExpandRatio(table, 1.0f);
	}

	private Component generateContent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);

		configureRootLayout(false);

		return rootLayout;
	}

	public Table getTable() {
		return table;
	}

	@Override
	public void focus() {
		searchField.focus();
	}

	public void setValue(TreeObject value) {
		table.setValue(value);
	}
}
