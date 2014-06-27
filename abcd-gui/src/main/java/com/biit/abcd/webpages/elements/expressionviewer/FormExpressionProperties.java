package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicSymbol;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExprOpMath;
import com.biit.abcd.persistence.entity.expressions.ExprOpValue;
import com.biit.abcd.persistence.entity.expressions.ExprSymbol;
import com.biit.abcd.persistence.entity.expressions.ExprValueFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExprValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExprValueString;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;

public class FormExpressionProperties extends PropertiesForClassComponent<FormExpression> {
	private static final long serialVersionUID = 7252467474326157874L;
	private static final int GRID_COLUMNS = 2;
	protected static final String buttonWidth = "80px";
	protected static final String formButtonWidth = "160px";
	private FormExpression formExpression;
	private Button deleteButton;

	public FormExpressionProperties() {
		super(FormExpression.class);
		createControlsTab();
		createBaseTab();
		createFormTab();
		createMathTab();
		createLogicalTab();
		createFunctionTab();
	}

	@Override
	protected void setElementAbstract(final FormExpression expression) {
		this.formExpression = expression;
	}

	private void createLogicalTab() {
		Button andButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_AND), new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		andButton.setWidth(buttonWidth);

		Button orButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_OR), new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		orButton.setWidth(buttonWidth);

		Button greaterThanButton = new Button(">", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		greaterThanButton.setWidth(buttonWidth);

		Button greaterEqualsButton = new Button("\u2265", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		greaterEqualsButton.setWidth(buttonWidth);

		Button lessThanButton = new Button("<", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		lessThanButton.setWidth(buttonWidth);

		Button lessEqualsButton = new Button("\u2264", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		lessEqualsButton.setWidth(buttonWidth);

		Button equalsButton = new Button("==", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		equalsButton.setWidth(buttonWidth);

		Button distinctButton = new Button("!=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		distinctButton.setWidth(buttonWidth);

		GridLayout exprLogicLayout = new GridLayout(GRID_COLUMNS, 4);
		exprLogicLayout.setWidth(null);
		exprLogicLayout.addComponent(andButton);
		exprLogicLayout.addComponent(orButton);
		exprLogicLayout.addComponent(greaterThanButton);
		exprLogicLayout.addComponent(greaterEqualsButton);
		exprLogicLayout.addComponent(lessThanButton);
		exprLogicLayout.addComponent(lessEqualsButton);
		exprLogicLayout.addComponent(equalsButton);
		exprLogicLayout.addComponent(distinctButton);

		addTab(exprLogicLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_LOGICAL), true);
	}

	private void createMathTab() {
		Button plusButton = new Button("+", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExprOpMath exprValue = new ExprOpMath();
				try {
					exprValue.setValue(ExprOpValue.PLUS);
					addExpression(exprValue);
				} catch (NotValidOperatorInExpression e) {

				}
			}
		});
		plusButton.setWidth(buttonWidth);

		Button minusButton = new Button("-", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		minusButton.setWidth(buttonWidth);

		Button multButton = new Button("*", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		multButton.setWidth(buttonWidth);

		Button divButton = new Button("/", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		divButton.setWidth(buttonWidth);

		Button moduleButton = new Button("%", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		moduleButton.setWidth(buttonWidth);

		Button potButton = new Button("x\u207F", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		potButton.setWidth(buttonWidth);

		GridLayout exprAtomicMathLayout = new GridLayout(GRID_COLUMNS, 4);
		exprAtomicMathLayout.setWidth(null);
		exprAtomicMathLayout.addComponent(plusButton);
		exprAtomicMathLayout.addComponent(minusButton);
		exprAtomicMathLayout.addComponent(multButton);
		exprAtomicMathLayout.addComponent(divButton);
		exprAtomicMathLayout.addComponent(moduleButton);
		exprAtomicMathLayout.addComponent(potButton);

		addTab(exprAtomicMathLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_MATH), true);
	}

	private void createFunctionTab() {
		Button notButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT), new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		notButton.setWidth(buttonWidth);

		Button maxButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MAX),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		maxButton.setWidth(buttonWidth);

		Button minimumButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MIN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		minimumButton.setWidth(buttonWidth);

		Button absoluteButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ABS),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		absoluteButton.setWidth(buttonWidth);

		Button sqrtButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_SQRT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		sqrtButton.setWidth(buttonWidth);

		Button roundButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ROUND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		roundButton.setWidth(buttonWidth);

		GridLayout exprFuncLayout = new GridLayout(GRID_COLUMNS, 4);
		exprFuncLayout.setWidth(null);
		exprFuncLayout.addComponent(maxButton);
		exprFuncLayout.addComponent(minimumButton);
		exprFuncLayout.addComponent(absoluteButton);
		exprFuncLayout.addComponent(sqrtButton);
		exprFuncLayout.addComponent(roundButton);

		addTab(exprFuncLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_FUNCTIONS), true);
	}

	private void createBaseTab() {
		Button leftBracketButton = new Button("(", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExprAtomicSymbol exprValue = new ExprAtomicSymbol();
				exprValue.setValue(ExprSymbol.LEFT_BRACKET);
				addExpression(exprValue);
			}
		});
		leftBracketButton.setWidth(buttonWidth);

		Button rightBracketButton = new Button(")", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExprAtomicSymbol exprValue = new ExprAtomicSymbol();
				exprValue.setValue(ExprSymbol.RIGHT_BRACKET);
				addExpression(exprValue);
			}
		});
		rightBracketButton.setWidth(buttonWidth);

		Button assignButton = new Button("=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExprOpMath exprValue = new ExprOpMath();
				try {
					exprValue.setValue(ExprOpValue.ASSIGNATION);
					addExpression(exprValue);
				} catch (NotValidOperatorInExpression e) {

				}
			}
		});
		assignButton.setWidth(buttonWidth);

		GridLayout exprBaseLayout = new GridLayout(GRID_COLUMNS, 4);
		exprBaseLayout.setWidth(null);
		exprBaseLayout.addComponent(leftBracketButton);
		exprBaseLayout.addComponent(rightBracketButton);
		exprBaseLayout.addComponent(assignButton);

		addTab(exprBaseLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_GENERIC), true);
	}

	private void createControlsTab() {
		deleteButton = new Button("\u232B");
		deleteButton.setWidth(buttonWidth);

		Button newLineButton = new Button("\u21B5", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		newLineButton.setWidth(buttonWidth);

		Button moveLeft = new Button("\u21A4", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		moveLeft.setWidth(buttonWidth);

		Button moveRight = new Button("\u21A6", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		moveRight.setWidth(buttonWidth);

		GridLayout exprControlsLayout = new GridLayout(GRID_COLUMNS, 4);
		exprControlsLayout.setWidth(null);
		exprControlsLayout.addComponent(moveLeft);
		exprControlsLayout.addComponent(moveRight);
		exprControlsLayout.addComponent(deleteButton);
		exprControlsLayout.addComponent(newLineButton);

		addTab(exprControlsLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_CONTROLS), true);
	}

	private void createFormTab() {
		Button globalConstantButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_CONSTANT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						SelectGlobalConstantsWindow globalWindow = new SelectGlobalConstantsWindow();
						globalWindow.showCentered();
						globalWindow.addAcceptAcctionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								GlobalVariable globalVariable = ((SelectGlobalConstantsWindow) window).getValue();
								if (globalVariable != null) {
									addExpression(new ExprValueGlobalConstant(globalVariable));
									window.close();
								} else {
									MessageManager.showError(ServerTranslate
											.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
								}
							}
						});
					}
				});
		globalConstantButton.setWidth(formButtonWidth);

		Button formVariableButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_VARIABLE),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						SelectFormElementVariableWindow variableWindow = new SelectFormElementVariableWindow();
						variableWindow.showCentered();
						variableWindow.addAcceptAcctionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								ExprValueFormCustomVariable formReference = ((SelectFormElementVariableWindow) window)
										.getValue();
								if (formReference != null) {
									addExpression(formReference);
									window.close();
								} else {
									MessageManager.showError(ServerTranslate
											.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
								}
							}
						});
					}
				});
		formVariableButton.setWidth(formButtonWidth);

		Button inputButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_INPUT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						StringInputWindow stringInputWindow = new StringInputWindow(
								ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
						stringInputWindow.setCaption(ServerTranslate
								.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_CAPTION));
						stringInputWindow.addAcceptAcctionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								String value = ((StringInputWindow) window).getValue();
								if (value == null || value.isEmpty()) {
									MessageManager.showError(ServerTranslate
											.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
								} else {
									ExprValueString exprValue = new ExprValueString(value);
									addExpression(exprValue);
									window.close();
								}
							}
						});
						stringInputWindow.showCentered();
					}
				});
		inputButton.setWidth(formButtonWidth);

		GridLayout exprFormLayout = new GridLayout(1, 4);
		exprFormLayout.setWidth(null);
		exprFormLayout.addComponent(globalConstantButton);
		exprFormLayout.addComponent(formVariableButton);
		exprFormLayout.addComponent(inputButton);

		addTab(exprFormLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_FORM), true);
	}

	private void addExpression(ExprBasic expression) {
		// formExpression.addExpression(expression);
		// firePropertyUpdateListener(formExpression);
		fireExpressionAddedListener(expression);
	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {

	}

	public void addDeleteExpressionButtonClickListener(ClickListener clickListener) {
		deleteButton.addClickListener(clickListener);
	}

}
