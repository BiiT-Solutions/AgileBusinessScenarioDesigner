package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.HashMap;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.SelectGlobalConstantsWindow;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ExpressionViewer extends CssLayout {
	private static final long serialVersionUID = -3032370197806581430L;
	public static String CLASSNAME = "v-expression-viewer";
	private FormExpression formExpression;
	private Expression selectedExpression = null;
	private VerticalLayout rootLayout;
	private HashMap<ExpressionElement, Expression> expressionOfElement;
	private Label evaluatorOutput;

	public ExpressionViewer() {
		setImmediate(true);
		expressionOfElement = new HashMap<>();
		setStyleName(CLASSNAME);
		addKeyController();
	}

	private void updateExpression() {
		if (formExpression != null) {
			updateExpression(formExpression);
		}
	}

	public void updateExpression(FormExpression formExpression) {
		// rootLayout.removeAllComponents();
		removeAllComponents();
		expressionOfElement = new HashMap<>();

		rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(false);
		rootLayout.setImmediate(true);
		rootLayout.setSizeFull();

		this.formExpression = formExpression;

		// Evaluator
		HorizontalLayout evaluatorLayout = createEvaluatorLayout();

		// One line for the expressions.
		HorizontalLayout lineLayout = new HorizontalLayout();
		lineLayout.setMargin(false);
		lineLayout.setSpacing(false);
		lineLayout.setImmediate(true);
		lineLayout.setSizeUndefined();

		addExpressions(lineLayout, formExpression.getExpressions());

		rootLayout.addComponent(evaluatorLayout);
		// If expand ratio is 0, component is not shown.
		rootLayout.setExpandRatio(evaluatorLayout, 0.00001f);
		rootLayout.setComponentAlignment(evaluatorLayout, Alignment.BOTTOM_RIGHT);

		rootLayout.addComponent(lineLayout);
		rootLayout.setExpandRatio(lineLayout, 0.99999f);
		updateExpressionSelectionStyles();

		addComponent(rootLayout);

		updateEvaluator();
	}

	private void addExpressions(HorizontalLayout lineLayout, List<Expression> expressions) {
		for (Expression expression : expressions) {
			addExpression(lineLayout, expression);
		}
	}

	public void addExpression(HorizontalLayout lineLayout, final Expression expression) {
		final ExpressionElement expressionElement = new ExpressionElement(expression.getExpressionTableString(),
				new LayoutClickListener() {
					private static final long serialVersionUID = -4305606865801828692L;

					@Override
					public void layoutClick(LayoutClickEvent event) {
						setSelectedExpression(expression);
						// Double click open operator popup.

						if (event.isDoubleClick()) {
							// For Operators.
							if (expression instanceof ExpressionOperator) {

								ChangeExpressionOperatorWindow operatorWindow = new ChangeExpressionOperatorWindow(
										expression);
								operatorWindow.showCentered();
								operatorWindow.addAcceptAcctionListener(new AcceptActionListener() {
									@Override
									public void acceptAction(AcceptCancelWindow window) {
										try {
											((ExpressionOperator) expression)
													.setValue(((ChangeExpressionOperatorWindow) window).getOperator());
											window.close();
											updateExpression();
											setSelectedExpression(expression);
										} catch (NotValidOperatorInExpression e) {
											e.printStackTrace();
											// Not possible
										}

									}
								});
								// For Strings.
							} else if (expression instanceof ExpressionValueString) {
								StringInputWindow stringInputWindow = new StringInputWindow(
										ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
								stringInputWindow.setCaption(ServerTranslate
										.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_CAPTION));
								stringInputWindow.setValue(((ExpressionValueString) expression).getValue());
								stringInputWindow.addAcceptAcctionListener(new AcceptActionListener() {
									@Override
									public void acceptAction(AcceptCancelWindow window) {
										String value = ((StringInputWindow) window).getValue();
										if (value == null || value.isEmpty()) {
											MessageManager.showError(ServerTranslate
													.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
										} else {
											// Update expression
											((ExpressionValueString) expression).setValue(value);
											window.close();
											updateExpression();
											setSelectedExpression(expression);
										}
									}
								});
								stringInputWindow.showCentered();
								// For Global constants
							} else if (expression instanceof ExpressionValueGlobalConstant) {
								SelectGlobalConstantsWindow globalWindow = new SelectGlobalConstantsWindow();
								globalWindow.showCentered();
								globalWindow.setValue(((ExpressionValueGlobalConstant) expression).getVariable());
								globalWindow.addAcceptAcctionListener(new AcceptActionListener() {
									@Override
									public void acceptAction(AcceptCancelWindow window) {
										GlobalVariable globalVariable = ((SelectGlobalConstantsWindow) window)
												.getValue();
										if (globalVariable != null) {
											((ExpressionValueGlobalConstant) expression).setVariable(globalVariable);
											window.close();
											updateExpression();
											setSelectedExpression(expression);
										} else {
											MessageManager.showError(ServerTranslate
													.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
										}
									}
								});
								// Form variables.
							} else if (expression instanceof ExpressionValueFormCustomVariable) {
								SelectFormElementVariableWindow variableWindow = new SelectFormElementVariableWindow();
								variableWindow.showCentered();
								variableWindow.setvalue((ExpressionValueFormCustomVariable) expression);
								variableWindow.addAcceptAcctionListener(new AcceptActionListener() {
									@Override
									public void acceptAction(AcceptCancelWindow window) {
										ExpressionValueFormCustomVariable formReference = ((SelectFormElementVariableWindow) window)
												.getValue();
										if (formReference != null) {
											// Update the already existing expression.
											((ExpressionValueFormCustomVariable) expression).setQuestion(formReference
													.getQuestion());
											((ExpressionValueFormCustomVariable) expression).setVariable(formReference
													.getVariable());
											window.close();
											updateExpression();
											setSelectedExpression(expression);
										} else {
											MessageManager.showError(ServerTranslate
													.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
										}
									}
								});
							}
						}
					}
				});

		expressionOfElement.put(expressionElement, expression);

		lineLayout.addComponent(expressionElement);
		lineLayout.setExpandRatio(expressionElement, 0);
		setSelectedExpression(expression);
	}

	private void setSelectedExpression(Expression expression) {
		selectedExpression = expression;
		updateExpressionSelectionStyles();
	}

	/**
	 * The selected expression is white.
	 */
	private void updateExpressionSelectionStyles() {
		for (int i = 0; i < rootLayout.getComponentCount(); i++) {
			if (rootLayout.getComponent(i) instanceof HorizontalLayout) {
				HorizontalLayout lineLayout = (HorizontalLayout) rootLayout.getComponent(i);
				for (int j = 0; j < lineLayout.getComponentCount(); j++) {
					if (lineLayout.getComponent(j) instanceof ExpressionElement) {
						if (expressionOfElement.get(lineLayout.getComponent(j)).equals(selectedExpression)) {
							lineLayout.getComponent(j).addStyleName("expression-selected");
						} else {
							lineLayout.getComponent(j).removeStyleName("expression-selected");
						}
					}
				}
			}
		}
	}

	public Expression getSelectedExpression() {
		return selectedExpression;
	}

	private void selectNextExpression() {
		if (getSelectedExpression() != null) {
			// Select next expression.
			int index = formExpression.getExpressions().indexOf(getSelectedExpression()) + 1;
			selectExpressionByIndex(index);
		}
	}

	private void selectPreviousExpression() {
		if (getSelectedExpression() != null) {
			// Select next expression.
			int index = formExpression.getExpressions().indexOf(getSelectedExpression()) - 1;
			selectExpressionByIndex(index);
		}
	}

	private void selectExpressionByIndex(int index) {
		Expression selected = null;
		if (index >= 0) {
			if (index < formExpression.getExpressions().size()) {
				selected = formExpression.getExpressions().get(index);
			} else if (!formExpression.getExpressions().isEmpty()) {
				selected = formExpression.getExpressions().get(formExpression.getExpressions().size() - 1);
			}
		} else {
			selectExpressionByIndex(0);
		}
		if (selected != null) {
			setSelectedExpression(selected);
		}
	}

	public void removeSelectedExpression() {
		if (getSelectedExpression() != null) {
			int index = formExpression.getExpressions().indexOf(getSelectedExpression());
			formExpression.getExpressions().remove(getSelectedExpression());
			updateExpression();
			selectExpressionByIndex(index);
		}
	}

	/**
	 * Adds a new element in the position of the selected element. Depending of the element, can be inserted after or
	 * before.
	 * 
	 * @param newElement
	 */
	public void addElementToSelected(Expression newElement) {
		// Checks if there is at least one expression
		if(formExpression != null){
			int index = formExpression.getExpressions().indexOf(getSelectedExpression()) + 1;
			if (newElement instanceof ExpressionSymbol) {
				// Brackets are added before selected expression in some cases.
				if (((ExpressionSymbol) newElement).getValue().getLeftSymbol() == true
						// Brackets always at right position in '<', '>', ... symbols.
						&& !(getSelectedExpression() instanceof ExpressionOperatorLogic)
						// Brackets always at right position in '=' symbol.
						&& (!(getSelectedExpression() instanceof ExpressionOperatorMath) || !((ExpressionOperatorMath) getSelectedExpression())
								.getValue().equals(AvailableOperator.ASSIGNATION))) {
					index--;
				}
			}
			if (index >= 0 && index < formExpression.getExpressions().size()) {
				formExpression.getExpressions().add(index, newElement);
			} else {
				formExpression.getExpressions().add(newElement);
			}
			updateExpression();
			setSelectedExpression(newElement);
		}else{
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.WARNING_EXPRESSION_TABLE_EMPTY);
		}
	}

	public FormExpression getFormExpression() {
		return formExpression;
	}

	private void updateEvaluator() {
		try {
			formExpression.getExpressionEvaluator().eval();
			evaluatorOutput.setStyleName("expression-valid");
			evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_VALID));
		} catch (Exception e) {
			AbcdLogger.debug(ExpressionViewer.class.getName(), e.getMessage());
			evaluatorOutput.setStyleName("expression-invalid");
			evaluatorOutput.setValue(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHECKER_INVALID));
		}
	}

	private HorizontalLayout createEvaluatorLayout() {
		HorizontalLayout checkerLayout = new HorizontalLayout();
		checkerLayout.setMargin(false);
		checkerLayout.setSpacing(false);
		checkerLayout.setSizeFull();

		evaluatorOutput = new Label();
		evaluatorOutput.setSizeUndefined();
		checkerLayout.addComponent(evaluatorOutput);
		checkerLayout.setComponentAlignment(evaluatorOutput, Alignment.TOP_RIGHT);

		return checkerLayout;
	}

	/**
	 * Add all keyboard defined actions.
	 */
	private void addKeyController() {
		this.addShortcutListener(new ShortcutListener("DELETE_SHORTCUT", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -71562151456777493L;

			@Override
			public void handleAction(Object sender, Object target) {
				removeSelectedExpression();
			}
		});

		this.addShortcutListener(new ShortcutListener("SELECT_NEXT", KeyCode.ARROW_RIGHT, null) {
			private static final long serialVersionUID = 7663105045629599269L;

			@Override
			public void handleAction(Object sender, Object target) {
				selectNextExpression();
			}
		});

		this.addShortcutListener(new ShortcutListener("SELECT_PREVIOUS", KeyCode.ARROW_LEFT, null) {
			private static final long serialVersionUID = 8453120978479798559L;

			@Override
			public void handleAction(Object sender, Object target) {
				selectPreviousExpression();
			}
		});

	}
}
