package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.biit.abcd.webpages.elements.decisiontable.DecissionTableQuestionAnswerPairComponent;
import com.biit.abcd.webpages.elements.decisiontable.SelectTableMenu;
import com.biit.abcd.webpages.elements.decisiontable.WindoNewTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class DecisionTableEditor extends FormWebPageComponent {
	static final long serialVersionUID = -5547452506556261601L;

	private DecissionTableQuestionAnswerPairComponent decisionTable;
	private DecisionTableEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectTableMenu tableSelectionMenu;

	public DecisionTableEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		// Create menu
		tableSelectionMenu = new SelectTableMenu();
		tableSelectionMenu.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				refreshDecisionTable();
			}

		});
		rootLayout.setMenu(tableSelectionMenu);

		// Create content
		decisionTable = new DecissionTableQuestionAnswerPairComponent();
		decisionTable.setSizeFull();

		rootLayout.setContent(decisionTable);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();

		// Save current state when changing window.
		addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -4725913087209115156L;

			@Override
			public void detach(DetachEvent event) {
				// Update diagram object if modified.
				updateForm();
			}

		});

		// Add tables
		for (TableRule tableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
			addTableRuleToMenu(tableRule);
		}
		sortTableMenu();

		// Select the first one if available.
		if (UserSessionHandler.getFormController().getForm().getTableRules().size() > 0) {
			tableSelectionMenu.setSelectedTableRule(UserSessionHandler.getFormController().getForm().getTableRules()
					.get(0));
		}

		refreshDecisionTable();
	}

	private void initUpperMenu() {
		final DecisionTableEditor thisPage = this;
		decisionTableEditorUpperMenu = new DecisionTableEditorUpperMenu();

		decisionTableEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8561092656527220053L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		decisionTableEditorUpperMenu.addNewTableClickListener(new ClickListener() {
			private static final long serialVersionUID = 4217977221393500979L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new WindoNewTable(thisPage));
			}

		});

		decisionTableEditorUpperMenu.addRemoveTableClickListener(new ClickListener() {
			private static final long serialVersionUID = 9216527027244131593L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelectedTable();
			}

		});

		decisionTableEditorUpperMenu.addNewConditionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2278600295153278706L;

			@Override
			public void buttonClick(ClickEvent event) {
				decisionTable.addColumnPair();
				if (decisionTable.getColumns().size() == 2 && decisionTable.getTableRules().isEmpty()) {
					decisionTable.addRow();
				}
			}
		});

		decisionTableEditorUpperMenu.addRemoveConditionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 4217977221393500979L;

			@Override
			public void buttonClick(ClickEvent event) {
				decisionTable.removeSelectedColumns();
			}
		});

		decisionTableEditorUpperMenu.addNewRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9216527027244131593L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (tableSelectionMenu.getSelectedTableRule() != null) {
					decisionTable.addRow();
				}
			}
		});

		decisionTableEditorUpperMenu.addRemoveRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8046509925666397195L;

			@Override
			public void buttonClick(ClickEvent event) {
				decisionTable.removeSelectedRows();
			}
		});

		setUpperMenu(decisionTableEditorUpperMenu);
	}

	/**
	 * Rules are not stored into the form automatically. We need to set them to
	 * the form before saving or changing the window.
	 */
	private void updateForm() {
		if (getSelectedTableRule() != null) {
			getSelectedTableRule().setRules(decisionTable.getDefinedTableRules());
		}
	}

	/**
	 * Saves all form information.
	 */
	private void save() {
		updateForm();
		try {
			UserSessionHandler.getFormController().save();
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(DecisionTableEditor.class.getName(), e);
		}
	}

	/**
	 * Updates the table where the user defines the rules with the information
	 * of the currently selected table.
	 */
	// TODO
	private void refreshDecisionTable() {
		decisionTable.removeAll();
		decisionTable.setTableRule(getSelectedTableRule());
		// if (getSelectedTableRule() != null) {
		// for (Question question :
		// getSelectedTableRule().getConditionsHeader()) {
		// decisionTable.addColumn(question);
		// }
		//
		// // Add table rows.
		// for (TableRuleRow tableRuleRow : getSelectedTableRule().getRules()) {
		// decisionTable.addRow(tableRuleRow);
		// }
		// }
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	/**
	 * Gets the currently selected table.
	 * 
	 * @return
	 */
	private TableRule getSelectedTableRule() {
		return tableSelectionMenu.getSelectedTableRule();
	}

	public void addTableRuleToMenu(TableRule tableRule) {
		tableSelectionMenu.addRow(tableRule);
		tableSelectionMenu.setSelectedTableRule(tableRule);
	}

	/**
	 * Sorts the table selector by the modification time of the objects.
	 */
	public void sortTableMenu() {
		tableSelectionMenu.sort();
	}

	/**
	 * Deletes the currently selected table.
	 */
	private void removeSelectedTable() {
		UserSessionHandler.getFormController().getForm().getTableRules()
				.remove(tableSelectionMenu.getSelectedTableRule());
		tableSelectionMenu.removeSelectedRow();
	}

}
