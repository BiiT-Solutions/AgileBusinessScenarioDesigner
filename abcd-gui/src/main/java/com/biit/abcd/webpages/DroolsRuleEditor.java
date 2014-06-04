package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionWindow;
import com.biit.abcd.webpages.elements.ruleeditor.RuleConditionTable;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class DroolsRuleEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private Form form;
	private RuleConditionTable decisionTable;

	public DroolsRuleEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(false);
		rootLayout.setMargin(true);

		Label whenLabel = new Label(ServerTranslate.tr(LanguageCodes.RULE_EDITOR_WHEN_LABEL));
		rootLayout.addComponent(whenLabel);

		HorizontalLayout conditionLayout = new HorizontalLayout();
		conditionLayout.setSizeFull();

		IconButton addQuestionButton = new IconButton(LanguageCodes.TREE_DESIGNER_QUESTION_ADD, ThemeIcons.ADD,
				LanguageCodes.TREE_DESIGNER_QUESTION_ADD, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = 4002268252434768032L;

					@Override
					public void buttonClick(ClickEvent event) {
						AddNewConditionWindow addNewConditionWindow = new AddNewConditionWindow(getForm(), false);
						addNewConditionWindow.addAcceptAcctionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								Question selectedQuestion = ((AddNewConditionWindow) window).getSelectedQuestion();
								decisionTable.addItem(selectedQuestion);
							}
						});
						addNewConditionWindow.showCentered();
					}
				});
		conditionLayout.addComponent(addQuestionButton);

		decisionTable = new RuleConditionTable();
		conditionLayout.addComponent(decisionTable);
		conditionLayout.setExpandRatio(decisionTable, 0.5f);

		ComboBox operationComboBox = new ComboBox();
		operationComboBox.addItem("AND");
		operationComboBox.addItem("OR");
		operationComboBox.addItem("()");
		operationComboBox.setNullSelectionAllowed(false);
		operationComboBox.setValue("AND");
		conditionLayout.addComponent(operationComboBox);

		rootLayout.addComponent(conditionLayout);
		rootLayout.setExpandRatio(conditionLayout, 0.5f);

		Label thenLabel = new Label(ServerTranslate.tr(LanguageCodes.RULE_EDITOR_THEN_LABEL));
		rootLayout.addComponent(thenLabel);

		TextArea textArea = new TextArea();
		textArea.setSizeFull();
		rootLayout.addComponent(textArea);
		rootLayout.setExpandRatio(textArea, 0.5f);

		// rootLayout.addComponent();

		getWorkingAreaLayout().addComponent(rootLayout);
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