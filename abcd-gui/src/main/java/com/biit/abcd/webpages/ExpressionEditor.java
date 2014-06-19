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
import com.biit.abcd.webpages.elements.expressiontree.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionTreeTable;
import com.biit.abcd.webpages.elements.formulaeditor.ExpressionEditorUpperMenu;
import com.biit.abcd.webpages.elements.formulaeditor.SelectExpressionTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ExpressionEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectExpressionTable tableSelectExpression;
	private ExpressionTreeTable thenTable;

	public ExpressionEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);
		
		// Create container
		HorizontalCollapsiblePanel rootLayout = new HorizontalCollapsiblePanel();
		rootLayout.setSizeFull();

		// Create menu
		tableSelectExpression = new SelectExpressionTable();
		tableSelectExpression.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				refreshExpressionEditor();
			}

		});
		rootLayout.setMenu(tableSelectExpression);

		// Create content
		expressionEditorComponent = new ExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();
		// expressionEditorComponent.addWhenExpression();
		thenTable = expressionEditorComponent.addThenExpression(ServerTranslate
				.tr(LanguageCodes.FORM_EXPRESSION_TABLE_NAME));
		rootLayout.setContent(expressionEditorComponent);

		getWorkingAreaLayout().addComponent(rootLayout);

		initUpperMenu();

		// Add tables
		tableSelectExpression.update(UserSessionHandler.getFormController().getForm());
		
		sortTableMenu();

		// Select the first one if available.
		if (UserSessionHandler.getFormController().getForm().getExpressions().size() > 0) {
			tableSelectExpression.setSelectedExpression(UserSessionHandler.getFormController().getForm()
					.getExpressions().get(0));
		}
		refreshExpressionEditor();
	}

	private void initUpperMenu() {
		final ExpressionEditor thisPage = this;

		decisionTableEditorUpperMenu = new ExpressionEditorUpperMenu();

		decisionTableEditorUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6036676119057486519L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		decisionTableEditorUpperMenu.addNewExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 377976184801401863L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new WindoNewExpression(thisPage));
			}

		});

		decisionTableEditorUpperMenu.addRemoveExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3561685413299735048L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelectedExpression();
			}

		});

		setUpperMenu(decisionTableEditorUpperMenu);
	}

	private ExprBasic getSelectedExpression() {
		return tableSelectExpression.getSelectedExpression();
	}

	private void refreshExpressionEditor() {
		thenTable.removeAll();
		if (getSelectedExpression() != null) {
			// Add table rows.
			thenTable.addExpression(getSelectedExpression());
		}
	}

	private void save() {
		try {
			UserSessionHandler.getFormController().save();
			UserSessionHandler.getFormController().reload();
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

	private void removeSelectedExpression() {
		UserSessionHandler.getFormController().getForm().getExpressions()
				.remove(tableSelectExpression.getSelectedExpression());
		tableSelectExpression.removeSelectedRow();
	}

	public void addExpressionToMenu(ExprBasic expression) {
		tableSelectExpression.addRow(expression);
		tableSelectExpression.setSelectedExpression(expression);
	}

	public void sortTableMenu() {
		tableSelectExpression.sort();
	}

}
