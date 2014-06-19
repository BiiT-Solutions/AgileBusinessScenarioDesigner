package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.elements.droolsrule.DroolsRuleEditorUpperMenu;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionTreeTable;
import com.biit.abcd.webpages.elements.expressiontree.RuleExpressionEditorComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class DroolsRuleEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private RuleExpressionEditorComponent ruleExpressionEditorComponent;
	private DroolsRuleEditorUpperMenu droolsRuleEditorUpperMenu;
	// private SelectExpressionTable tableSelectExpression;
	private ExpressionTreeTable thenTable;

	public DroolsRuleEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		// Create menu
		// tableSelectExpression = new SelectExpressionTable();
		// tableSelectExpression.addValueChangeListener(new
		// ValueChangeListener() {
		// private static final long serialVersionUID = -7103550436798085895L;
		//
		// @Override
		// public void valueChange(ValueChangeEvent event) {
		// refreshExpressionEditor();
		// }
		//
		// });
		// rootLayout.setMenu(tableSelectExpression);

		// Create content
		ruleExpressionEditorComponent = new RuleExpressionEditorComponent();
		ruleExpressionEditorComponent.setSizeFull();
		rootLayout.setContent(ruleExpressionEditorComponent);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();

		// Add tables
		for (ExprBasic expression : UserSessionHandler.getFormController().getForm().getExpressions()) {
			addExpressionToMenu(expression);
		}

		sortTableMenu();

		// Select the first one if available.
		// if
		// (UserSessionHandler.getFormController().getForm().getExpressions().size()
		// > 0) {
		// tableSelectExpression.setSelectedExpression(UserSessionHandler.getFormController().getForm()
		// .getExpressions().get(0));
		// }
		// refreshExpressionEditor();
	}

	private void initUpperMenu() {
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

	private ExprBasic getSelectedRule() {
		// TODO
		return null;
	}

	private void refreshExpressionEditor() {
		thenTable.removeAll();
		if (getSelectedRule() != null) {
			// Add table rows.
			thenTable.addExpression(getSelectedRule());
		}
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
		// TODO
	}

	public void addExpressionToMenu(ExprBasic expression) {
		// TODO
	}

	public void sortTableMenu() {
		// TODO
	}
}