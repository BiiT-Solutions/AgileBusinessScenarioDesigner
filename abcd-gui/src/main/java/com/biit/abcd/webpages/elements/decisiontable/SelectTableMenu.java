package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectTableMenu extends Table {
	private static final long serialVersionUID = -5723571725991709050L;

	enum MenuProperties {
		TABLE_NAME;
	};

	public SelectTableMenu() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		setColumnCollapsingAllowed(false);

		addContainerProperty(MenuProperties.TABLE_NAME, TableRule.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_NAME), null, Align.LEFT);
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(TableRule tableRule) {
		Item item = addItem(tableRule);
		item.getItemProperty(MenuProperties.TABLE_NAME).setValue(tableRule);
	}

	public void removeSelectedRow() {
		TableRule tableRule = (TableRule) getValue();
		if (tableRule != null) {
			removeItem(tableRule);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (TableRule tableRule : form.getTableRules()) {
			addRow(tableRule);
		}
	}

	public TableRule getSelectedTableRule() {
		return (TableRule) getValue();
	}

	public void setSelectedTableRule(TableRule tableRule) {
		setValue(tableRule);
	}
	
	

}
