package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.components.SelectExpressionTableEditable;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.SimpleExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.WindowNewExpression;
import com.biit.abcd.webpages.elements.formulaeditor.ExpressionEditorUpperMenu;
import com.biit.form.exceptions.DependencyExistException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ExpressionEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionEditorUpperMenu decisionTableEditorUpperMenu;
	private SelectExpressionTableEditable tableSelectExpression;

	public ExpressionEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		// Create container
		HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
		collapsibleLayout.setSizeFull();

		// Create menu
		tableSelectExpression = new SelectExpressionTableEditable();
		tableSelectExpression.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -7103550436798085895L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				UserSessionHandler.getFormController().setLastAccessExpression(getSelectedExpression());
				refreshExpressionEditor();
			}

		});
		collapsibleLayout.createMenu(tableSelectExpression);

		// Create content
		expressionEditorComponent = new SimpleExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();
		collapsibleLayout.setContent(expressionEditorComponent);

		getWorkingAreaLayout().addComponent(collapsibleLayout);

		initUpperMenu();

		if (UserSessionHandler.getFormController().getForm() != null) {
			// Add tables
			tableSelectExpression.update(UserSessionHandler.getFormController().getForm());

			sortTableMenu();

			if (UserSessionHandler.getFormController().getLastAccessExpression() != null) {
				tableSelectExpression.setSelectedExpression(UserSessionHandler.getFormController()
						.getLastAccessExpression());
			} else {
				// Select the first one if available.
				if (UserSessionHandler.getFormController().getForm().getExpressionChain().size() > 0) {
					tableSelectExpression.setSelectedExpression(UserSessionHandler.getFormController().getForm()
							.getExpressionChain().get(0));
				}
			}
			refreshExpressionEditor();
		} else {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
		}
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
				UI.getCurrent().addWindow(
						new WindowNewExpression(thisPage, LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
								LanguageCodes.WINDOW_NEW_EXPRESSION_TEXTFIELD));
			}

		});

		decisionTableEditorUpperMenu.addRemoveExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3561685413299735048L;

			@Override
			public void buttonClick(ClickEvent event) {
				final AlertMessageWindow windowAccept = new AlertMessageWindow(
						LanguageCodes.WARNING_EXPRESSION_DELETION);
				windowAccept.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						ExpressionChain expChain = getSelectedExpression();
						try {
							CheckDependencies.checkExpressionDependencies(UserSessionHandler.getFormController().getForm(),
									expChain);
							removeSelectedExpression();
							AbcdLogger.info(this.getClass().getName(),
									"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
											+ expChain.getClass() + " with 'Name: " + expChain.getName() + "'.");
							windowAccept.close();
						} catch (DependencyExistException e) {
							// Forbid the remove action if exist dependency.
							MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
									LanguageCodes.EXPRESSION_DESIGNER_WARNING_CANNOT_REMOVE_EXPRESSION);
						}
					}
				});
				windowAccept.showCentered();
			}
		});

		setUpperMenu(decisionTableEditorUpperMenu);
	}

	private ExpressionChain getSelectedExpression() {
		return tableSelectExpression.getSelectedExpression();
	}

	private void refreshExpressionEditor() {
		((SimpleExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(getSelectedExpression());
		expressionEditorComponent.updateSelectionStyles();
	}

	private void save() {
		try {
			UserSessionHandler.getFormController().save();
			MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
		} catch (Exception e) {
			System.out.println(e);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(DecisionTableEditor.class.getName(), e);
		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void removeSelectedExpression() {
		UserSessionHandler.getFormController().getForm().getExpressionChain()
				.remove(tableSelectExpression.getSelectedExpression());
		tableSelectExpression.removeSelectedRow();
		refreshExpressionEditor();
	}

	public void addExpressionToMenu(ExpressionChain expression) {
		tableSelectExpression.addRow(expression);
		tableSelectExpression.setSelectedExpression(expression);
	}

	public void sortTableMenu() {
		tableSelectExpression.sort();
	}

	public void selectComponent(ExpressionChain element) {
		tableSelectExpression.setSelectedExpression(element);
	}

}
