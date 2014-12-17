package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SelectExpressionTableEditable extends TableCellLabelEdit {
	private static final long serialVersionUID = 3348987098295904893L;

	public SelectExpressionTableEditable() {
		super(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_NAME, LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_UPDATE);
	}

	public void update(Form form) {
		this.removeAllItems();
		for (ExpressionChain expression : form.getExpressionChains()) {
			addRow(expression);
		}
	}

	public ExpressionChain getSelectedExpression() {
		return (ExpressionChain) getValue();
	}

	public void setSelectedExpression(ExpressionChain expression) {
		setValue(expression);
	}

	@Override
	protected EditCellComponent setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		EditCellComponent editCellComponent = super.setDefaultNewItemPropertyValues(itemId, item);
		if (editCellComponent != null) {
			editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((ExpressionChain) itemId));
		}
		return null;
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private ExpressionChain formExpression;

		public CellEditButtonClickListener(ExpressionChain formExpression) {
			this.formExpression = formExpression;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final TableCellLabelEditWindow newTableCellEditWindow = new TableCellLabelEditWindow(
					ServerTranslate.translate(LanguageCodes.WINDOW_EDIT_TABLE_CELL_LABEL));

			newTableCellEditWindow.setValue(formExpression.getName());
			newTableCellEditWindow.showCentered();
			newTableCellEditWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					for (ExpressionChain existingTableRule : UserSessionHandler.getFormController().getForm()
							.getExpressionChains()) {
						if (existingTableRule != formExpression
								&& existingTableRule.getName().equals(newTableCellEditWindow.getValue())) {
							MessageManager.showError(LanguageCodes.ERROR_REPEATED_EXPRESSION_NAME);
							return;
						}
					}
					formExpression.setName(newTableCellEditWindow.getValue());
					formExpression.setUpdateTime();
					updateItemTableRuleInGui(formExpression);
					newTableCellEditWindow.close();
				}
			});
		}
	}
}
