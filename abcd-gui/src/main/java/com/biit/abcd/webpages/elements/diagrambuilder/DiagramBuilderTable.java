package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class DiagramBuilderTable extends Table {
	private static final long serialVersionUID = -2420087674159328133L;

	enum Properties {
		DIAGRAM_NAME
	};

	public DiagramBuilderTable() {
		super();
		addContainerProperty(Properties.DIAGRAM_NAME, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_DIAGRAM_BUILDER_TABLE_DIAGRAM_NAME), null, Align.LEFT);
	}

	@SuppressWarnings("unchecked")
	public void addDiagram(Diagram diagram) {
		Item item = addItem(diagram);
		item.getItemProperty(Properties.DIAGRAM_NAME).setValue(diagram.getName());
	}
}
