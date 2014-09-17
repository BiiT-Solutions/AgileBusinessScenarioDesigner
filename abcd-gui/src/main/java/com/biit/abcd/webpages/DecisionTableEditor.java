package com.biit.abcd.webpages;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionUnit;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AcceptCancelWindow.CancelActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectTableRuleTableEditable;
import com.biit.abcd.webpages.components.WindowSelectDateUnit;
import com.biit.abcd.webpages.elements.decisiontable.AddNewActionExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewAnswerExpressionWindow;
import com.biit.abcd.webpages.elements.decisiontable.AddNewQuestionEditorWindow;
import com.biit.abcd.webpages.elements.decisiontable.ClearActionListener;
import com.biit.abcd.webpages.elements.decisiontable.ClearExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.biit.abcd.webpages.elements.decisiontable.EditActionListener;
import com.biit.abcd.webpages.elements.decisiontable.EditExpressionListener;
import com.biit.abcd.webpages.elements.decisiontable.NewDecisionTable;
import com.biit.abcd.webpages.elements.decisiontable.WindoNewTable;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
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
				Iterator<TableRule> iterator = (UserSessionHandler.getFormController().getForm().getTableRules()
						.iterator());
				tableSelectionMenu.setSelectedTableRule(iterator.next());
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
				final AlertMessageWindow windowAccept = new AlertMessageWindow(
						LanguageCodes.WARNING_TABLE_RULE_DELETION);
				windowAccept.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						TableRule tableRule = tableSelectionMenu.getSelectedTableRule();
						try {
							CheckDependencies.checkTableRuleDependencies(UserSessionHandler.getFormController()
									.getForm(), tableRule);
							removeSelectedTable();
							AbcdLogger.info(this.getClass().getName(),
									"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
											+ tableRule.getClass() + " with 'Name: " + tableRule.getName() + "'.");
							windowAccept.close();
						} catch (DependencyExistException e) {
							// Forbid the remove action if exist dependency.
							MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
									LanguageCodes.TABLE_RULE_DESIGNER_WARNING_CANNOT_REMOVE_TABLE_RULE);
						}

					}
				});
				windowAccept.showCentered();
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

					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has added new conditions to '"
							+ tableRule.getName() + "''.");
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
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has removed conditions from '"
							+ tableRule.getName() + "''.");
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

					AbcdLogger.info(this.getClass().getName(),
							"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has added a new row to '"
									+ tableRule.getName() + "''.");
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

					AbcdLogger.info(this.getClass().getName(),
							"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a row from '"
									+ tableRule.getName() + "''.");
				}
			}
		});

		decisionTableEditorUpperMenu.addCopyRowsClickListener(new ClickListener() {
			private static final long serialVersionUID = -189428121286122030L;

			@Override
			public void buttonClick(ClickEvent event) {
				copy();
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has copied rows from '" + tableSelectionMenu.getSelectedTableRule().getName() + "''.");
			}
		});
		decisionTableEditorUpperMenu.addPasteRowsClickListener(new ClickListener() {
			private static final long serialVersionUID = 4749689189249879942L;

			@Override
			public void buttonClick(ClickEvent event) {
				paste();
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has pasted rows from '" + tableSelectionMenu.getSelectedTableRule().getName() + "''.");
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
	 * Updates the table where the user defines the rules with the information of the currently selected table.
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
		decisionTable.removeAll();
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
		int conditions = decisionTable.getColumns().size() / 2;

		decisionTable.removeSelectedRows(tableRule);
		if (decisionTable.getTableSize(tableRule) == 0) {
			addNewRow(tableRule);
			for (int i = 0; i < conditions; i++) {
				addNewColumnPair(tableRule);
			}
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
		final ExpressionChain answerExpression = (ExpressionChain) decisionTable.getNextCellValue(row, propertyId);

		final AddNewQuestionEditorWindow newQuestionConditionWindow = new AddNewQuestionEditorWindow(UserSessionHandler
				.getFormController().getForm(), false);

		if (decisionTable.getCellValue(row, propertyId) instanceof ExpressionValueCustomVariable) {
			final ExpressionValueCustomVariable variableExpression = (ExpressionValueCustomVariable) decisionTable
					.getCellValue(row, propertyId);

			if (variableExpression != null) {
				newQuestionConditionWindow.select(variableExpression);
			}
		} else if (decisionTable.getCellValue(row, propertyId) instanceof ExpressionValueTreeObjectReference) {
			final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
					.getCellValue(row, propertyId);

			if (questionExpression.getReference() != null) {
				newQuestionConditionWindow.select(questionExpression.getReference());
			}
		}

		newQuestionConditionWindow.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				// final Question originalQuestion = (Question)
				// questionExpression.getReference();
				final Object selectedElement = ((AddNewQuestionEditorWindow) window).getSelectedCondition();
				if (selectedElement != null) {
					newQuestionConditionWindow.close();
					if (selectedElement instanceof Question) {
						final Question selectedQuestion = (Question) selectedElement;
						if (selectedQuestion.getAnswerFormat() == AnswerFormat.DATE) {
							// Create a window for selecting the unit and assign
							// it to the expression.
							final WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));
							windowDate.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									// removeAnswerExpressionIfNeeded(originalQuestion,
									// selectedQuestion, answerExpression);
									answerExpression.removeAllExpressions();
									setQuestionDateExpression(row, (Integer) propertyId, selectedQuestion,
											windowDate.getValue());
									AbcdLogger.info(this.getClass().getName(), "User '"
											+ UserSessionHandler.getUser().getEmailAddress() + "' has added Question '"
											+ selectedQuestion.getName() + "' to Table rule '"
											+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
									window.close();
								}
							});
							windowDate.showCentered();
						} else {
							answerExpression.removeAllExpressions();
							AbcdLogger.info(this.getClass().getName(), "User '"
									+ UserSessionHandler.getUser().getEmailAddress() + "' has added Question '"
									+ selectedQuestion.getName() + "' to Table rule '"
									+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
							setTreeObjectExpression(row, (Integer) propertyId, selectedQuestion);
						}

					} else if (selectedElement instanceof TreeObject) {
						TreeObject treeObject = (TreeObject) selectedElement;
						answerExpression.removeAllExpressions();
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has added the tree object '"
								+ treeObject.getName() + "' to Table rule '"
								+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
						setTreeObjectExpression(row, (Integer) propertyId, treeObject);

					} else if (selectedElement instanceof ExpressionValueCustomVariable) {
						ExpressionValueCustomVariable customVariable = (ExpressionValueCustomVariable) selectedElement;
						TreeObject treeObject = customVariable.getReference();
						answerExpression.removeAllExpressions();
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has added the custom variable '"
								+ customVariable.getRepresentation() + "' to Table rule '"
								+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
						setCustomVariableExpression(row, (Integer) propertyId, treeObject, customVariable.getVariable());

					} else if (selectedElement instanceof GenericTreeObjectType) {
						GenericTreeObjectType genericType = (GenericTreeObjectType) selectedElement;
						answerExpression.removeAllExpressions();
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has added the generic type '"
								+ genericType.getExpressionName() + "' to Table rule '"
								+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
						setGenericTreeObjectExpression(row, (Integer) propertyId, genericType);

					} else if (selectedElement instanceof ExpressionValueGenericCustomVariable) {
						ExpressionValueGenericCustomVariable customVariable = (ExpressionValueGenericCustomVariable) selectedElement;
						GenericTreeObjectType genericTO = customVariable.getType();
						answerExpression.removeAllExpressions();
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has added the custom variable '"
								+ customVariable.getRepresentation() + "' to Table rule '"
								+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
						setGenericCustomVariableExpression(row, (Integer) propertyId, genericTO,
								customVariable.getVariable());
					}
				} else {
					MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
				}
			}
		});
		newQuestionConditionWindow.showCentered();
	}

	/**
	 * Sets a tree object as an expression inside the table
	 * 
	 * @param row
	 * @param propertyId
	 * @param selectedObject
	 */
	private void setTreeObjectExpression(TableRuleRow row, Integer propertyId, TreeObject selectedObject) {
		row.setExpression(propertyId, new ExpressionValueTreeObjectReference(selectedObject));
		decisionTable.update(getSelectedTableRule());
	}

	/**
	 * Sets a custom variable as an expression inside the table
	 * 
	 * @param row
	 * @param propertyId
	 * @param treeObject
	 * @param customVariable
	 */
	private void setCustomVariableExpression(TableRuleRow row, Integer propertyId, TreeObject treeObject,
			CustomVariable customVariable) {
		row.setExpression(propertyId, new ExpressionValueCustomVariable(treeObject, customVariable));
		decisionTable.update(getSelectedTableRule());
	}

	/**
	 * Sets a custom variable as an expression inside the table
	 * 
	 * @param row
	 * @param propertyId
	 * @param treeObject
	 * @param customVariable
	 */
	private void setGenericTreeObjectExpression(TableRuleRow row, Integer propertyId, GenericTreeObjectType genericType) {
		row.setExpression(propertyId, new ExpressionValueGenericVariable(genericType));
		decisionTable.update(getSelectedTableRule());
	}

	/**
	 * Sets a custom variable as an expression inside the table
	 * 
	 * @param row
	 * @param propertyId
	 * @param treeObject
	 * @param customVariable
	 */
	private void setGenericCustomVariableExpression(TableRuleRow row, Integer propertyId,
			GenericTreeObjectType genericType, CustomVariable customVariable) {
		row.setExpression(propertyId, new ExpressionValueGenericCustomVariable(genericType, customVariable));
		decisionTable.update(getSelectedTableRule());
	}

	private void setQuestionDateExpression(TableRuleRow row, Integer propertyId, Question selectedQuestion,
			QuestionUnit unit) {
		row.setExpression(propertyId, new ExpressionValueTreeObjectReference(selectedQuestion, unit));
		decisionTable.update(getSelectedTableRule());
	}

	private void newEditAnswerWindow(TableRuleRow row, Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getPreviousCellValue(row, propertyId);
		final ExpressionChain answerExpression = (ExpressionChain) decisionTable.getCellValue(row, propertyId);

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

					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has added Answer '"
							+ answerExpression.getName() + "' regarding Question '"
							+ questionExpression.getReference().getName() + "' in Table rule '"
							+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");

					newActionValueWindow.close();
				}
			});
			newActionValueWindow.addCancelActionListener(new CancelActionListener() {
				@Override
				public void cancelAction(AcceptCancelWindow window) {
					newActionValueWindow.removeFirstExpression();
				}
			});
		} else {
			MessageManager.showWarning(LanguageCodes.WARNING_NO_QUESTION_SELECTED_CAPTION,
					LanguageCodes.WARNING_NO_QUESTION_SELECTED_BODY);
		}
	}

	private void removeQuestion(TableRuleRow row, Object propertyId) {
		ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getCellValue(row, propertyId);
		TreeObject auxTo = questionExpression.getReference();
		questionExpression.setReference(null);
		// Removes and updates the table.
		removeAnswer(row, (Integer) propertyId + 1);

		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' has removed Question '" + auxTo.getName() + "' from Table rule '"
				+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
	}

	private void removeAnswer(TableRuleRow row, Object propertyId) {
		final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) decisionTable
				.getPreviousCellValue(row, propertyId);
		final ExpressionChain answerExpression = (ExpressionChain) decisionTable.getCellValue(row, propertyId);
		answerExpression.removeAllExpressions();
		decisionTable.update(getSelectedTableRule());

		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' has removed Answer '" + answerExpression.getName() + "' regarding Question '"
				+ questionExpression.getReference().getName() + "' in Table rule '"
				+ tableSelectionMenu.getSelectedTableRule().getName() + "''.");
	}

	@Override
	public void editAction(final TableRuleRow row) {
		if (row.getActionChain() != null) {
			final AddNewActionExpressionWindow newActionValueWindow = new AddNewActionExpressionWindow(
					row.getActionChain());

			newActionValueWindow.showCentered();
			newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					ExpressionChain expChain = newActionValueWindow.getExpressionChain();

					if (expChain != null) {
						row.getActionChain().setExpressions(expChain.getExpressions());
						decisionTable.update(getSelectedTableRule());

						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has added Action '"
								+ row.getActionChain().getRepresentation() + "' to row '" + row.getId()
								+ "' in Table rule '" + tableSelectionMenu.getSelectedTableRule().getName() + "''.");
					}
					newActionValueWindow.close();
				}
			});
		}
	}

	@Override
	public void removeAction(TableRuleRow row) {
		ExpressionChain action = row.getActionChain();
		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' has removed Action '" + action.getRepresentation() + "' from row '" + row.getId()
				+ "' in Table rule '" + tableSelectionMenu.getSelectedTableRule().getName() + "''.");
		row.getActionChain().removeAllExpressions();
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
