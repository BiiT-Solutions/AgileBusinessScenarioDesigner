package com.biit.abcd.webpages;

import java.util.List;
import java.util.Set;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionWindow;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableComponent;
import com.biit.abcd.webpages.elements.decisiontable.DecisionTableEditorUpperMenu;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class DecisionTableEditor extends FormWebPageComponent {
	static final long serialVersionUID = -5547452506556261601L;

	private Form form;
	private DecisionTableComponent decisionTable;
	private DecisionTableEditorUpperMenu decisionTableEditorUpperMenu;

	public DecisionTableEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		decisionTable = new DecisionTableComponent();
		decisionTable.setSizeFull();

		rootLayout.setContent(decisionTable);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();
	}

	private void initUpperMenu() {
		decisionTableEditorUpperMenu = new DecisionTableEditorUpperMenu();

		decisionTableEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8561092656527220053L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

		decisionTableEditorUpperMenu.addNewConditionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2278600295153278706L;

			@Override
			public void buttonClick(ClickEvent event) {
				AddNewConditionWindow addNewConditionWindow = new AddNewConditionWindow(getForm(), true);
				addNewConditionWindow.disableQuestions(decisionTable.getColumns());
				addNewConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Set<Question> selectedQuestions = ((AddNewConditionWindow) window).getSelectedQuestions();
						for (Question selectedQuestion : selectedQuestions) {
							((AddNewConditionWindow) window).disableQuestion(selectedQuestion);
							decisionTable.addColumn(selectedQuestion);
							if (decisionTable.getColumns().size() == 1 && decisionTable.getNumberOfRules() == 0) {
								decisionTable.addRow();
							}
						}
						window.close();
					}
				});
				addNewConditionWindow.showCentered();
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
				decisionTable.addRow();
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

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

}
