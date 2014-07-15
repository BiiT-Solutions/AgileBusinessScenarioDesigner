package com.biit.abcd.webpages;

import java.util.Collection;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.AnswerExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectAnswerWindow;
import com.biit.abcd.webpages.components.SelectTableRuleTableEditable;
import com.biit.abcd.webpages.elements.decisiontable.AddNewActionExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewAnswerExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionWindow;
import com.biit.abcd.webpages.elements.decisiontable.ClearActionListener;
import com.biit.abcd.webpages.elements.decisiontable.ClearExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.biit.abcd.webpages.elements.decisiontable.EditActionListener;
import com.biit.abcd.webpages.elements.decisiontable.EditExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.NewActionTable;
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
	private int i=0;

	public DecisionTableEditor() {
		super();
	}

	@Override
	protected void initContent() {
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
				i++;
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

	private void newEditQuestionWindow(TableRuleRow row, Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = decisionTable.getExpressionValue(row, propertyId);
		final ExpressionValueTreeObjectReference answerExpression = decisionTable.getNextExpressionValue(row,
				propertyId);

		final AddNewConditionWindow newConditionWindow = new AddNewConditionWindow(UserSessionHandler
				.getFormController().getForm(), false);

		if (questionExpression.getReference() != null) {
			newConditionWindow.setTreeObjectSelected(questionExpression.getReference());
		}
		newConditionWindow.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
				if(answerExpression instanceof AnswerExpression){
					// TODO
				} else{
					ExpressionValueTreeObjectReference answerTreeObject = answerExpression;
					Answer answerToQuestion = (Answer) answerTreeObject.getReference();
					if ((selectedQuestion == null) || (!selectedQuestion.contains(answerToQuestion))){
						answerTreeObject.setReference(null);
					}
				}
				questionExpression.setReference(selectedQuestion);
				decisionTable.update(getSelectedTableRule());
				newConditionWindow.close();
			}
		});
		newConditionWindow.showCentered();
	}

	private void newEditAnswerWindow(TableRuleRow row, Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = decisionTable.getPreviousExpressionValue(row,
				propertyId);
		final ExpressionValueTreeObjectReference answerExpression = decisionTable.getExpressionValue(row, propertyId);

		//TODO
		if (questionExpression.getReference() != null) {
			Question question = (Question) questionExpression.getReference();
			if(question.getAnswerType().equals(AnswerType.INPUT)){
				final AnswerExpression answerChain = (AnswerExpression)answerExpression;
				try {
					final AddNewAnswerExpressionWindow newActionValueWindow = new AddNewAnswerExpressionWindow(answerChain);
					newActionValueWindow.showCentered();
					newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							try {
								ExpressionChain expChain = newActionValueWindow.getExpressionChain();
								if(expChain != null){
									answerChain.setExpressionChain(expChain);
									decisionTable.update(getSelectedTableRule());
								}
								newActionValueWindow.close();
							} catch (NotValidExpression e) {
								MessageManager.showError(e.getMessage());
							}
						}
					});

				} catch (NotValidExpression e1) {
					MessageManager.showError(e1.getMessage());
					AbcdLogger.errorMessage(NewDecisionTable.class.getName(), e1);
				}
			}else{

				final ExpressionValueTreeObjectReference answerTreeObject = answerExpression;
				final SelectAnswerWindow newAnswerValueWindow = new SelectAnswerWindow(question);
				if (answerTreeObject.getReference() != null) {
					newAnswerValueWindow.setTreeObjectSelected(answerTreeObject.getReference());
				}
				newAnswerValueWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Answer selectedanswer = ((SelectAnswerWindow) window).getSelectedTableValue();
						answerTreeObject.setReference(selectedanswer);
						decisionTable.update(getSelectedTableRule());
						newAnswerValueWindow.close();
					}
				});
				newAnswerValueWindow.showCentered();
			}
		} else {
			MessageManager.showError(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
					LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
		}
	}

	private void removeQuestion(TableRuleRow row, Object propertyId) {
		ExpressionValueTreeObjectReference questionExpression = decisionTable.getExpressionValue(row, propertyId);
		questionExpression.setReference(null);
		removeAnswer(row, (Integer)propertyId+1);
		decisionTable.update(getSelectedTableRule());
	}

	private void removeAnswer(TableRuleRow row, Object propertyId) {
		ExpressionValueTreeObjectReference answerExpression = decisionTable.getExpressionValue(row, propertyId);
		if(answerExpression instanceof AnswerExpression){
			AnswerExpression answerInput = (AnswerExpression)answerExpression;
			try {
				answerInput.setExpressionChain(null);
			} catch (NotValidExpression e) {
				e.printStackTrace();
			}
		}else{
			answerExpression.setReference(null);
		}
		decisionTable.update(getSelectedTableRule());
	}

	@Override
	public void editAction(final TableRuleRow row) {
		try {
			if (!row.getActions().isEmpty()) {
				final AddNewActionExpressionWindow newActionValueWindow = new AddNewActionExpressionWindow(row
						.getActions().get(0));

				newActionValueWindow.showCentered();
				newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						try {
							ExpressionChain expChain = newActionValueWindow.getExpressionChain();
							if (expChain != null) {
								row.getActions().get(0).setExpressionChain(expChain);
								decisionTable.update(getSelectedTableRule());
							}
							newActionValueWindow.close();
						} catch (NotValidExpression e) {
							MessageManager.showError(e.getMessage());
						}
					}
				});
			}
		} catch (NotValidExpression e1) {
			MessageManager.showError(e1.getMessage());
			AbcdLogger.errorMessage(NewActionTable.class.getName(), e1);
		}
	}

	@Override
	public void removeAction(TableRuleRow row) {
		try {
			row.getActions().get(0).setExpressionChain("");
			decisionTable.update(getSelectedTableRule());
		} catch (NotValidExpression e) {
			MessageManager.showError(e.getMessage());
		}
	}

	public void copy() {
		if(getSelectedTableRule()==null){
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_COPY_ROW_NOT_PERFORMED_CAPTION, LanguageCodes.DECISION_TABLE_COPY_TABLE_NOT_SELECTED);
			return;
		}
		Collection<TableRuleRow> rows = decisionTable.getSelectedRules();
		if(rows.isEmpty()){
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_COPY_ROW_NOT_PERFORMED_CAPTION, LanguageCodes.DECISION_TABLE_COPY_NO_SELECTED_ELEMENTS);
			return;
		}
		UserSessionHandler.getFormController().copyTableRuleRows(getSelectedTableRule(),rows);
	}

	public void paste() {
		if(getSelectedTableRule()==null){
			MessageManager.showWarning(LanguageCodes.DECISION_TABLE_PASTE_ROW_NOT_PERFORMED_CAPTION, LanguageCodes.DECISION_TABLE_COPY_TABLE_NOT_SELECTED);
			return;
		}
		UserSessionHandler.getFormController().pasteTableRuleRowsAsNew(getSelectedTableRule());
		decisionTable.update(getSelectedTableRule());
	}
}
