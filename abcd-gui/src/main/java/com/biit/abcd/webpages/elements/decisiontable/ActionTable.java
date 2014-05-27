package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.decisiontable.DecisionRule;
import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class ActionTable extends Table {

	private static final long serialVersionUID = -8737505874064899775L;

	enum Columns {
		ACTION
	};

	public ActionTable() {
		setImmediate(true);
		setSizeFull();
		addContainerProperty(Columns.ACTION, Component.class, null,
				ServerTranslate.tr(LanguageCodes.ACTION_TABLE_HEADER_ACTION), null, Align.CENTER);

	}

	public void addItem(DecisionRule rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule,super.addItem(rule));
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(Object itemId, Item item) {
		for (Object propertyId : getContainerPropertyIds()) {
			if (item.getItemProperty(propertyId).getValue() == null) {
				EditCellComponent editCellComponent = new EditCellComponent();
				//Propagate element click.
				editCellComponent.addLayoutClickListener(new LayoutClickListener() {
					private static final long serialVersionUID = -8606373054437936380L;
					@Override
					public void layoutClick(LayoutClickEvent event) {
						//TODO
					}
				});
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}
}
