package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectTableRuleTable extends Table {
	private static final long serialVersionUID = -5723571725991709050L;

	enum MenuProperties {
		TABLE_NAME, UPDATE_TIME;
	};

	public SelectTableRuleTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();
		
		addContainerProperty(MenuProperties.TABLE_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_UPDATE), null, Align.LEFT);
		
		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.TABLE_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.TABLE_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);
		
		setSortContainerPropertyId(MenuProperties.UPDATE_TIME);
		setSortAscending(false);
		sort();
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(TableRule tableRule) {
		Item item = addItem(tableRule);
		item.getItemProperty(MenuProperties.TABLE_NAME).setValue(tableRule.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(DateManager.convertDateToStringWithHours(tableRule.getUpdateTime()));
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
