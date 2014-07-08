package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectDroolsRule extends Table {
	private static final long serialVersionUID = 3348987098295904893L;

	enum MenuProperties {
		RULE_NAME, UPDATE_TIME;
	};

	public SelectDroolsRule() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		addContainerProperty(MenuProperties.RULE_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.DROOLS_RULES_EDITOR_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.DROOLS_RULES_EDITOR_TABLE_COLUMN_UPDATE), null, Align.LEFT);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.RULE_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.RULE_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.UPDATE_TIME);
		setSortAscending(false);
		sort();
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(Rule rule) {
		Item item = addItem(rule);
		item.getItemProperty(MenuProperties.RULE_NAME).setValue(rule.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToString(rule.getUpdateTime()));
	}

	public void removeSelectedRow() {
		Rule rule = (Rule) getValue();
		if (rule != null) {
			removeItem(rule);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (Rule rule : form.getRules()) {
			addRow(rule);
		}
	}

	public Rule getSelectedRule() {
		return (Rule) getValue();
	}

	public void setSelectedExpression(Rule rule) {
		setValue(rule);
	}
}
