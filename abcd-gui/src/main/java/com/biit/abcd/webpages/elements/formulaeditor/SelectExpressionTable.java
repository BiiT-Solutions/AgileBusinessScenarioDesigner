package com.biit.abcd.webpages.elements.formulaeditor;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
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

		addContainerProperty(MenuProperties.EXPRESSION_NAME, ExprBasic.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_UPDATE), null, Align.LEFT);

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
	public void addRow(ExprBasic expression) {
		Item item = addItem(expression);
		item.getItemProperty(MenuProperties.EXPRESSION_NAME).setValue(expression);
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToString(expression.getUpdateTime()));
	}

	public void removeSelectedRow() {
		ExprBasic expression = (ExprBasic) getValue();
		if (expression != null) {
			removeItem(expression);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (ExprBasic expression : form.getExpressions()) {
			addRow(expression);
		}
	}

	public ExprBasic getSelectedExpression() {
		return (ExprBasic) getValue();
	}

	public void setSelectedExpression(ExprBasic expression) {
		setValue(expression);
	}
}
