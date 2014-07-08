package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectDiagramTable extends Table {
	private static final long serialVersionUID = -2420087674159328133L;

	enum MenuProperties {
		DIAGRAM_NAME, UPDATE_TIME;
	};

	public SelectDiagramTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		addContainerProperty(MenuProperties.DIAGRAM_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_DIAGRAM_BUILDER_TABLE_DIAGRAM_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_DIAGRAM_BUILDER_TABLE_DIAGRAM_UPDATE), null, Align.LEFT);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.DIAGRAM_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.DIAGRAM_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.UPDATE_TIME);
		setSortAscending(false);
		sort();

	}

	@SuppressWarnings("unchecked")
	public void addDiagram(Diagram diagram) {
		Item item = addItem(diagram);
		item.getItemProperty(MenuProperties.DIAGRAM_NAME).setValue(diagram.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToString(diagram.getUpdateTime()));
	}
	
	public void addRow(Diagram diagram){
		addDiagram(diagram);
	}

	public Diagram getSelectedDiagram() {
		return (Diagram) getValue();
	}
}
