package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.Expressions;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectExpressionTable extends Table {
	private static final long serialVersionUID = 3348987098295904893L;

	enum MenuProperties {
		EXPRESSION_NAME, UPDATE_TIME;
	};

	public SelectExpressionTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		addContainerProperty(MenuProperties.EXPRESSION_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_UPDATE), null, Align.LEFT);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.EXPRESSION_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.EXPRESSION_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.UPDATE_TIME);
		setSortAscending(false);
		sort();
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(Expressions expression) {
		Item item = addItem(expression);
		item.getItemProperty(MenuProperties.EXPRESSION_NAME).setValue(expression.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToString(expression.getUpdateTime()));
	}

	public void removeSelectedRow() {
		Expression expression = (Expression) getValue();
		if (expression != null) {
			removeItem(expression);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (Expressions expression : form.getFormExpressions()) {
			addRow(expression);
		}
	}

	public Expressions getSelectedExpression() {
		return (Expressions) getValue();
	}

	public void setSelectedExpression(Expressions expression) {
		setValue(expression);
	}
}
