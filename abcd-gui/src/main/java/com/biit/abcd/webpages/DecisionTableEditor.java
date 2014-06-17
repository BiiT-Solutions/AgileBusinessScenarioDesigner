package com.biit.abcd.webpages;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionWindow;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableComponent;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.biit.abcd.webpages.elements.decisiontable.SelectTableMenu;
import com.biit.abcd.webpages.elements.decisiontable.WindoNewTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class DecisionTableEditor extends FormWebPageComponent {
	static final long serialVersionUID = -5547452506556261601L;

	private DecisionTableComponent decisionTable;
	private DecisionTableEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectTableMenu rightMenu;

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
		rightMenu = new SelectTableMenu();
		rightMenu.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				refreshDecisionTable();
			}

		});
		rootLayout.setMenu(rightMenu);

		// Create content
		decisionTable = new DecisionTableComponent();
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
				if (rightMenu.getSelectedTableRule() != null) {
					AddNewConditionWindow addNewConditionWindow = new AddNewConditionWindow(UserSessionHandler
							.getFormController().getForm(), true);
					addNewConditionWindow.disableQuestions(decisionTable.getColumns());
					addNewConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							Set<Question> selectedQuestions = ((AddNewConditionWindow) window).getSelectedQuestions();
							for (Question selectedQuestion : selectedQuestions) {
								((AddNewConditionWindow) window).disableQuestion(selectedQuestion);
								decisionTable.addColumn(selectedQuestion);
								getSelectedTableRule().getConditions().add(selectedQuestion);
								if (decisionTable.getColumns().size() == 1 && decisionTable.getTableRules().isEmpty()) {
									decisionTable.addRow();
								}
							}
							window.close();
						}
					});
					addNewConditionWindow.showCentered();
				}
			}
		});

		decisionTableEditorUpperMenu.addRemoveConditionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 4217977221393500979L;

			@Override
			public void buttonClick(ClickEvent event) {
				Collection<Question> removedQuestions = decisionTable.removeSelectedColumns();
				for (Question question : removedQuestions) {
					getSelectedTableRule().getConditions().remove(question);
				}
			}
		});

		decisionTableEditorUpperMenu.addNewRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9216527027244131593L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (rightMenu.getSelectedTableRule() != null) {
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

	private void updateForm() {
		if (getSelectedTableRule() != null) {
			getSelectedTableRule().setRules(decisionTable.getDefinedTableRules());
		}
	}

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

	@Override
	public void setForm(Form form) {
		// Add table columns
		// if (UserSessionHandler.getFormController().getForm().getTableRules().isEmpty()) {
		// UserSessionHandler.getFormController().getForm().getTableRules().add(new TableRule());
		// }

		// Add tables
		for (TableRule tableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
			addTableRuleToMenu(tableRule);
		}

		// Select the first one if available.
		if (UserSessionHandler.getFormController().getForm().getTableRules().size() > 0) {
			rightMenu.setSelectedTableRule(UserSessionHandler.getFormController().getForm().getTableRules().get(0));
		}

		refreshDecisionTable();
	}

	private void refreshDecisionTable() {
		decisionTable.removeAll();
		if (getSelectedTableRule() != null) {
			if (!getSelectedTableRule().getRules().isEmpty()) {
				for (Question question : getSelectedTableRule().getRules().get(0).getConditions().keySet()) {
					decisionTable.addColumn(question);
				}
			}

			// Add table rows.
			for (TableRuleRow tableRuleRow : getSelectedTableRule().getRules()) {
				decisionTable.addRow(tableRuleRow);
			}
		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private TableRule getSelectedTableRule() {
		return rightMenu.getSelectedTableRule();
	}

	public void addTableRuleToMenu(TableRule tableRule) {
		rightMenu.addRow(tableRule);
		rightMenu.setSelectedTableRule(tableRule);
	}

	public void removeSelectedTable() {
		UserSessionHandler.getFormController().getForm().getTableRules().remove(rightMenu.getSelectedTableRule());
		rightMenu.removeSelectedRow();
	}

}
