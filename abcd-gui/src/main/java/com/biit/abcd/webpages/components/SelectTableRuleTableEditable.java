package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.elements.decisiontable.EditCellComponent;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SelectTableRuleTableEditable extends TableCellLabelEdit {
	private static final long serialVersionUID = -4379803887335057366L;

	public SelectTableRuleTableEditable() {
		super(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_NAME, LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_UPDATE);
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
	
	protected EditCellComponent setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		EditCellComponent editCellComponent = super.setDefaultNewItemPropertyValues(itemId, item);
		if (editCellComponent != null) {
			editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((TableRule) itemId));
		}
		return null;
	}
	
	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private TableRule tableRule;

		public CellEditButtonClickListener(TableRule tableRule) {
			this.tableRule = tableRule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final TableCellLabelEditWindow newTableCellEditWindow = new TableCellLabelEditWindow(
					ServerTranslate
					.translate(LanguageCodes.WINDOW_EDIT_TABLE_CELL_LABEL));

			newTableCellEditWindow.setValue(tableRule.getName());
			newTableCellEditWindow.showCentered();
			newTableCellEditWindow.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					for (TableRule existingTableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
						if (existingTableRule.getName().equals(newTableCellEditWindow.getValue())) {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_TABLE_RULE_NAME);
							return;
						}
					}
					tableRule.setName(newTableCellEditWindow.getValue());
					tableRule.setUpdateTime();
					updateItemTableRuleInGui(tableRule);
					newTableCellEditWindow.close();
				}
			});
		}
	}
}
