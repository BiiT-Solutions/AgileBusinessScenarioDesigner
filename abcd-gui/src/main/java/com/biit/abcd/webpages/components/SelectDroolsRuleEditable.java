package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.TestingId;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SelectDroolsRuleEditable extends TableCellLabelEdit {
	private static final long serialVersionUID = 3348987098295904893L;

	public SelectDroolsRuleEditable() {
		super(LanguageCodes.DROOLS_RULES_EDITOR_TABLE_COLUMN_NAME,
				LanguageCodes.DROOLS_RULES_EDITOR_TABLE_COLUMN_UPDATE);
		setId(TestingId.RULE_TABLE.getValue());
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

	protected EditCellComponent setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		EditCellComponent editCellComponent = super.setDefaultNewItemPropertyValues(itemId, item);
		if (editCellComponent != null) {
			editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((Rule) itemId));
		}
		return null;
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private Rule rule;

		public CellEditButtonClickListener(Rule rule) {
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final TableCellLabelEditWindow newTableCellEditWindow = new TableCellLabelEditWindow(
					ServerTranslate.translate(LanguageCodes.WINDOW_EDIT_TABLE_CELL_LABEL));

			newTableCellEditWindow.setValue(rule.getName());
			newTableCellEditWindow.showCentered();
			newTableCellEditWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					for (Rule existingDroolsRule : UserSessionHandler.getFormController().getForm().getRules()) {
						if (existingDroolsRule != rule
								&& existingDroolsRule.getName().equals(newTableCellEditWindow.getValue())) {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_DROOLS_RULE_NAME);
							return;
						}
					}
					rule.setName(newTableCellEditWindow.getValue());
					rule.setUpdateTime();
					updateItemTableRuleInGui(rule);
					newTableCellEditWindow.close();
				}
			});
		}
	}
}
