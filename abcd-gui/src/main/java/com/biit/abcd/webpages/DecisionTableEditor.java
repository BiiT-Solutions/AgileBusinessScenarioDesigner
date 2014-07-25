package com.biit.abcd.webpages;

import java.util.Collection;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.DateUnit;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueDateTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectTableRuleTableEditable;
import com.biit.abcd.webpages.components.WindowSelectDateUnit;
import com.biit.abcd.webpages.elements.decisiontable.AddNewActionExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewAnswerExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionWindow;
import com.biit.abcd.webpages.elements.decisiontable.ClearActionListener;
import com.biit.abcd.webpages.elements.decisiontable.ClearExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.biit.abcd.webpages.elements.decisiontable.EditActionListener;
import com.biit.abcd.webpages.elements.decisiontable.EditExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.NewDecisionTable;
import com.biit.abcd.webpages.elements.decisiontable.WindoNewTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class DecisionTableEditor extends FormWebPageComponent implements EditExpressionListener,
		ClearExpressionListener, EditActionListener, ClearActionListener {
	static final long serialVersionUID = -5547452506556261601L;

	private NewDecisionTable decisionTable;
	private DecisionTableEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectTableRuleTableEditable tableSelectionMenu;

	public DecisionTableEditor() {
		super();
	}

	@Override
	protected void initContent() {
		// If there is no form, then go back to form manager.
		if (UserSessionHandler.getFormController().getForm() == null) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
			return;
		}

		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel(false);
		rootLayout.setSizeFull();

		// Create menu
		tableSelectionMenu = new SelectTableRuleTableEditable();
		tableSelectionMenu.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				UserSessionHandler.getFormController().setLastAccessTable(tableSelectionMenu.getSelectedTableRule());
				refreshDecisionTable();
			}
		});
		rootLayout.createMenu(tableSelectionMenu);

		// Create content
		decisionTable = new NewDecisionTable();
		decisionTable.setSizeFull();
		// Add cell function listeners
		decisionTable.addEditExpressionListener(this);
		decisionTable.addClearExpressionListener(this);
		decisionTable.addEditActionListener(this);
		decisionTable.addClearActionListener(this);

		rootLayout.setContent(decisionTable);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();

		// Add tables
		for (TableRule tableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
			addTableRuleToMenu(tableRule);
		}
		sortTableMenu();

		// Select the last access object or the first one
		if (UserSessionHandler.getFormController().getLastAccessTable() != null) {
			tableSelectionMenu.setSelectedTableRule(UserSessionHandler.getFormController().getLastAccessTable());
		} else {
			// Select the first one if available.
			if (UserSessionHandler.getFormController().getForm().getTableRules().size() > 0) {
				tableSelectionMenu.setSelectedTableRule(UserSessionHandler.getFormController().getForm()
						.getTableRules().get(0));
			}
		}
		addShortcutListener(new ShortcutListener("copy", KeyCode.C, new int[] { ModifierKey.CTRL }) {
			private static final long serialVersionUID = -368776333862758815L;

			@Override
			public void handleAction(Object sender, Object target) {
				copy();
			}
		});
		addShortcutListener(new ShortcutListener("paste", KeyCode.V, new int[] { ModifierKey.CTRL }) {
			private static final long serialVersionUID = -368776333862758815L;

			@Override
			public void handleAction(Object sender, Object target) {
				paste();
			}
		});
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
				UI.getCurrent().addWindow(
						new WindoNewTable(thisPage, LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
								LanguageCodes.WINDOW_NEW_TABLE_NAME_TEXTFIELD));
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
				TableRule tableRule = tableSelectionMenu.getSelectedTableRule();
				if (tableRule != null) {
					addNewCondition(tableRule);
					tableRule.setUpdateTime();
				}
			}
		});

		decisionTableEditorUpperMenu.addRemoveConditionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 4217977221393500979L;

			@Override
			public void buttonClick(ClickEvent event) {
				TableRule tableRule = tableSelectionMenu.getSelectedTableRule();
				if (tableRule != null) {
					removeCondition(tableRule);
					tableRule.setUpdateTime();
				}
			}
		});

		decisionTableEditorUpperMenu.addNewRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9216527027244131593L;

			@Override
			public void buttonClick(ClickEvent event) {
				TableRule tableRule = tableSelectionMenu.getSelectedTableRule();
				if (tableRule != null) {
					addNewRow(tableRule);
					tableRule.setUpdateTime();
				}
			}
		});

		decisionTableEditorUpperMenu.addRemoveRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8046509925666397195L;

			@Override
			public void buttonClick(ClickEvent event) {
				TableRule tableRule = tableSelectionMenu.getSelectedTableRule();
				if (tableRule != null) {
					removeRow(tableRule);
					tableRule.setUpdateTime();
				}
			}
		});

		decisionTableEditorUpperMenu.addCopyRowsClickListener(new ClickListener() {
			private static final long serialVersionUID = -189428121286122030L;

			@Override
			public void buttonClick(ClickEvent event) {
				copy();
			}
		});
		decisionTableEditorUpperMenu.addPasteRowsClickListener(new ClickListener() {
			private static final long serialVersionUID = 4749689189249879942L;

			@Override
			public void buttonClick(ClickEvent event) {
				paste();
			}
		});
		setUpperMenu(decisionTableEditorUpperMenu);
	}

	/**
	 * Saves all form information.
	 */
	private void save() {
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
	private void refreshDecisionTable() {
		decisionTable.update(getSelectedTableRule());
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
	}

	public void addTablefromWindow(TableRule tableRule) {
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

	public void selectComponent(TableRule element) {
		tableSelectionMenu.setSelectedTableRule(element);
	}

	private void addNewCondition(TableRule tableRule) {
		if ((decisionTable.getColumns().size() == 0) && (tableRule.getRules().isEmpty())) {
			addNewRow(tableRule);
		}
		addNewColumnPair(tableRule);
		decisionTable.update(tableRule);
	}

	private void addNewColumnPair(TableRule tableRule) {
		tableRule.addEmptyExpressionPair();
		decisionTable.update(tableRule);
	}

	private void removeCondition(TableRule tableRule) {
		decisionTable.removeSelectedColumns(tableRule);
		decisionTable.update(tableRule);
	}

	private void addNewRow(TableRule tableRule) {
		TableRuleRow row = tableRule.addRow();
		decisionTable.addRow(row);
	}

	private void removeRow(TableRule tableRule) {
		decisionTable.removeSelectedRows(tableRule);
		if (decisionTable.getTableSize(tableRule) == 0) {
			addNewRow(tableRule);
			addNewColumnPair(tableRule);
		}
		decisionTable.update(tableRule);
	}

	@Override
	public void editExpression(TableRuleRow row, Object propertyId) {
		if (((Integer) propertyId % 2) == 0) {
			newEditQuestionWindow(row, propertyId);
		} else {
			newEditAnswerWindow(row, propertyId);
		}
	}

	@Override
	public void clearExpression(TableRuleRow row, Object propertyId) {
		if (((Integer) propertyId % 2) == 0) {
			removeQuestion(row, propertyId);
		} else {
			removeAnswer(row, propertyId);
		}
	}

	private void newEditQuestionWindow(final TableRuleRow row, final Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getExpressionValue(row, propertyId);
		final ExpressionChain answerExpression = (ExpressionChain) decisionTable
				.getNextExpressionValue(row, propertyId);

		final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
				.getFormController().getForm(), false);

		if (questionExpression.getReference() != null) {
			newConditionWindow.setTreeObjectSelected(questionExpression.getReference());
		}
		newConditionWindow.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				final Question originalQuestion = (Question) questionExpression.getReference();
				final Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();

				if (selectedQuestion != null) {
					newConditionWindow.close();
					if (selectedQuestion.getAnswerFormat() == AnswerFormat.DATE) {
						// Create a window for selecting the unit and assign it
						// to the expression.
						final WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
								.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));
						windowDate.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								removeAnswerExpressionIfNeeded(originalQuestion, selectedQuestion, answerExpression);
								whatever(row, (Integer) propertyId, selectedQuestion,windowDate.getValue());
								window.close();
							}
						});
						windowDate.showCentered();
					} else {
						removeAnswerExpressionIfNeeded(originalQuestion, selectedQuestion, answerExpression);
						whatever(row, (Integer) propertyId, selectedQuestion);
					}
				} else {
					MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
				}
			}
		});
		newConditionWindow.showCentered();
	}

	private void removeAnswerExpressionIfNeeded(Question originalQuestion, Question selectedQuestion,
			ExpressionChain answerExpression) {
		if (originalQuestion != null && selectedQuestion != null) {
			if (((originalQuestion.getAnswerType() == AnswerType.INPUT) && (selectedQuestion.getAnswerType() != AnswerType.INPUT))
					|| ((originalQuestion.getAnswerType() != AnswerType.INPUT) && (selectedQuestion.getAnswerType() == AnswerType.INPUT))
					|| (!originalQuestion.equals(selectedQuestion))) {
				answerExpression.removeAllExpressions();
			}
		}
	}

	private void whatever(TableRuleRow row, Integer propertyId, Question selectedQuestion) {
		ExpressionValueTreeObjectReference reference;

		reference = new ExpressionValueTreeObjectReference(selectedQuestion);

		row.setExpression(propertyId, reference);
		decisionTable.update(getSelectedTableRule());
	}
	
	private void whatever(TableRuleRow row, Integer propertyId, Question selectedQuestion, DateUnit dateUnit) {
		ExpressionValueTreeObjectReference reference;

		reference = new ExpressionValueDateTreeObjectReference(selectedQuestion,dateUnit);

		row.setExpression(propertyId, reference);
		decisionTable.update(getSelectedTableRule());
	}

	private void newEditAnswerWindow(TableRuleRow row, Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getPreviousExpressionValue(row, propertyId);
		final ExpressionChain answerExpression = (ExpressionChain) decisionTable.getExpressionValue(row, propertyId);

		if (questionExpression.getReference() != null) {
			final AddNewAnswerExpressionWindow newActionValueWindow = new AddNewAnswerExpressionWindow(
					questionExpression, answerExpression);
			newActionValueWindow.showCentered();
			newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					ExpressionChain expChain = newActionValueWindow.getExpressionChain();
					answerExpression.setExpressions(expChain.getExpressions());
					decisionTable.update(getSelectedTableRule());
					newActionValueWindow.close();
				}
			});
		} else {
			MessageManager.showError(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
					LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
		}
	}

	private void removeQuestion(TableRuleRow row, Object propertyId) {
		ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getExpressionValue(row, propertyId);
		questionExpression.setReference(null);
		// Removes and updates the table.
		removeAnswer(row, (Integer) propertyId + 1);
	}

	private void removeAnswer(TableRuleRow row, Object propertyId) {
		ExpressionChain expressionChain = (ExpressionChain) decisionTable.getExpressionValue(row, propertyId);
		expressionChain.removeAllExpressions();
		decisionTable.update(getSelectedTableRule());
	}

	@Override
	public void editAction(final TableRuleRow row) {
		if (row.getAction() != null) {
			final AddNewActionExpressionWindow newActionValueWindow = new AddNewActionExpressionWindow(row.getAction());

			newActionValueWindow.showCentered();
			newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					ExpressionChain expChain = newActionValueWindow.getExpressionChain();

					if (expChain != null) {
						row.getAction().setExpressions(expChain.getExpressions());
						decisionTable.update(getSelectedTableRule());
					}
					newActionValueWindow.close();
				}
			});
		}
	}

	@Override
	public void removeAction(TableRuleRow row) {
		row.getAction().removeAllExpressions();
		decisionTable.update(getSelectedTableRule());
	}

	public void copy() {
		if (getSelectedTableRule() == null) {
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_COPY_ROW_NOT_PERFORMED_CAPTION,
					LanguageCodes.DECISION_TABLE_COPY_TABLE_NOT_SELECTED);
			return;
		}
		Collection<TableRuleRow> rows = decisionTable.getSelectedRules();
		if (rows.isEmpty()) {
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_COPY_ROW_NOT_PERFORMED_CAPTION,
					LanguageCodes.DECISION_TABLE_COPY_NO_SELECTED_ELEMENTS);
			return;
		}
		UserSessionHandler.getFormController().copyTableRuleRows(getSelectedTableRule(), rows);
	}

	public void paste() {
		if (getSelectedTableRule() == null) {
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_PASTE_ROW_NOT_PERFORMED_CAPTION,
					LanguageCodes.DECISION_TABLE_COPY_TABLE_NOT_SELECTED);
			return;
		}
		UserSessionHandler.getFormController().pasteTableRuleRowsAsNew(getSelectedTableRule());
		decisionTable.update(getSelectedTableRule());
	}
}
