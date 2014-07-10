package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectDroolsRuleEditable;
import com.biit.abcd.webpages.elements.droolsrule.ConditionActionEditor;
import com.biit.abcd.webpages.elements.droolsrule.DroolsRuleEditorUpperMenu;
import com.biit.abcd.webpages.elements.droolsrule.WindowNewRule;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class DroolsRuleEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -1017932957756165996L;
	private ExpressionEditorComponent ruleExpressionEditorComponent;
	private DroolsRuleEditorUpperMenu droolsRuleEditorUpperMenu;
	private SelectDroolsRuleEditable tableSelectRule;

	public DroolsRuleEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
		collapsibleLayout.setSizeFull();

		// Create menu
		tableSelectRule = new SelectDroolsRuleEditable();
		tableSelectRule.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				refreshRuleEditor();
			}

		});
		collapsibleLayout.createMenu(tableSelectRule);

		// Create content
		ruleExpressionEditorComponent = new ConditionActionEditor();
		ruleExpressionEditorComponent.setSizeFull();
		collapsibleLayout.setContent(ruleExpressionEditorComponent);

		getWorkingAreaLayout().addComponent(collapsibleLayout);

		initUpperMenu();

		// Add expressions
		for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
			addRuleToTableMenu(rule);
		}

		sortTableMenu();

		// Select the first one if available.
		if (UserSessionHandler.getFormController().getForm().getRules().size() > 0) {
			tableSelectRule.setSelectedExpression(UserSessionHandler.getFormController().getForm().getRules().get(0));
		}
		refreshRuleEditor();
	}

	private void initUpperMenu() {
		final DroolsRuleEditor thisPage = this;
		droolsRuleEditorUpperMenu = new DroolsRuleEditorUpperMenu();

		droolsRuleEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6036676119057486519L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		droolsRuleEditorUpperMenu.addNewRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 377976184801401863L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new WindowNewRule(thisPage, LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR,
								LanguageCodes.DROOLS_RULES_EDITOR_NEW_RULE_TEXTFIELD));
			}
		});

		droolsRuleEditorUpperMenu.addRemoveRuleButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3561685413299735048L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelectedRule();
			}

		});

		setUpperMenu(droolsRuleEditorUpperMenu);
	}

	private Rule getSelectedRule() {
		return tableSelectRule.getSelectedRule();
	}

	private void refreshRuleEditor() {
		((ConditionActionEditor) ruleExpressionEditorComponent).refreshRuleEditor(getSelectedRule());
		ruleExpressionEditorComponent.updateSelectionStyles();
	}

	private void save() {
		try {
			UserSessionHandler.getFormController().save();
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(DecisionTableEditor.class.getName(), e);
		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void removeSelectedRule() {
		UserSessionHandler.getFormController().getForm().getRules().remove(tableSelectRule.getSelectedRule());
		tableSelectRule.removeSelectedRow();
		refreshRuleEditor();
	}

	public void addRuleToTableMenu(Rule rule) {
		tableSelectRule.addRow(rule);
		tableSelectRule.setSelectedExpression(rule);
	}

	public void sortTableMenu() {
		tableSelectRule.sort();
	}
}