package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.TreeObject;
import com.vaadin.data.Container.Filterable;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Custom component that takes a table and a filter with IFilterContainsText as
 * interface and creates a visualization that filters the table when the input
 * in the search field changes.
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
	
	public void setValue(TreeObject value){
		table.setValue(value);
	}
}
