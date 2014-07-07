package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;

public class TabOperatorLayout extends TabLayout {
	private static final long serialVersionUID = -6980953550185164306L;
	private static final int GRID_COLUMNS = 3;
	private GridLayout expLayout;

	public TabOperatorLayout() {
		expLayout = new GridLayout(GRID_COLUMNS, 4);
		expLayout.setWidth("100%");

		createMathOperators();
		createMathFunctionsOperators();
		createBaseTab();
		createLogicalOperators();
		createLogicalFunctionsOperators();
		createInputField();

		addComponent(expLayout);
		setComponentAlignment(expLayout, Alignment.MIDDLE_CENTER);
	}

	private void createMathOperators() {
		Button plusButton = createButton("+", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionOperatorMath exprValue = new ExpressionOperatorMath();
				try {
					exprValue.setValue(AvailableOperator.PLUS);
					addExpression(exprValue);
				} catch (NotValidOperatorInExpression e) {

				}
			}
		});

		Button minusButton = createButton("-", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button multButton = createButton("*", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button divButton = createButton("/", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button moduleButton = createButton("%", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button potButton = createButton("x\u207F", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		expLayout.addComponent(plusButton);
		expLayout.addComponent(minusButton);
		expLayout.addComponent(multButton);
		expLayout.addComponent(divButton);
		expLayout.addComponent(moduleButton);
		expLayout.addComponent(potButton);
	}

	private void createMathFunctionsOperators() {

		Button maxButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MAX),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionFunction exprValue = new ExpressionFunction();
						exprValue.setValue(AvailableFunction.MAX);
						addExpression(exprValue);
					}
				});

		Button minimumButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MIN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

		Button absoluteButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ABS),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

		Button sqrtButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_SQRT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

		Button roundButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ROUND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

		expLayout.addComponent(maxButton);
		expLayout.addComponent(minimumButton);
		expLayout.addComponent(absoluteButton);
		expLayout.addComponent(sqrtButton);
		expLayout.addComponent(roundButton);
	}

	private void createLogicalFunctionsOperators() {
		Button notButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		Button inButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_IN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionFunction exprValue = new ExpressionFunction();
						exprValue.setValue(AvailableFunction.IN);
						addExpression(exprValue);
					}
				});

		Button betweenButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_BETWEEN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionFunction exprValue = new ExpressionFunction();
						exprValue.setValue(AvailableFunction.BETWEEN);
						addExpression(exprValue);
					}
				});

		expLayout.addComponent(notButton);
		expLayout.addComponent(inButton);
		expLayout.addComponent(betweenButton);
	}

	private void createBaseTab() {
		Button leftBracketButton = createButton("(", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.LEFT_BRACKET);
				addExpression(exprValue);
			}
		});

		Button rightBracketButton = createButton(")", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.RIGHT_BRACKET);
				addExpression(exprValue);
			}
		});

		Button assignButton = createButton("=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionOperatorMath exprValue = new ExpressionOperatorMath();
				try {
					exprValue.setValue(AvailableOperator.ASSIGNATION);
					addExpression(exprValue);
				} catch (NotValidOperatorInExpression e) {

				}
			}
		});

		Button commaButton = createButton(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.COMMA);
				addExpression(exprValue);
			}
		});
		expLayout.addComponent(leftBracketButton);
		expLayout.addComponent(rightBracketButton);
		expLayout.addComponent(assignButton);
		expLayout.addComponent(commaButton);
	}

	private void createLogicalOperators() {
		Button andButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_AND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionOperatorLogic exprValue = new ExpressionOperatorLogic();
						try {
							exprValue.setValue(AvailableOperator.AND);
							addExpression(exprValue);
						} catch (NotValidOperatorInExpression e) {

						}
					}
				});

		Button orButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_OR),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

		Button greaterThanButton = createButton(">", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button greaterEqualsButton = createButton("\u2265", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button lessThanButton = createButton("<", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button lessEqualsButton = createButton("\u2264", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button equalsButton = createButton("==", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		Button distinctButton = createButton("!=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});

		expLayout.addComponent(andButton);
		expLayout.addComponent(orButton);
		expLayout.addComponent(greaterThanButton);
		expLayout.addComponent(greaterEqualsButton);
		expLayout.addComponent(lessThanButton);
		expLayout.addComponent(lessEqualsButton);
		expLayout.addComponent(equalsButton);
		expLayout.addComponent(distinctButton);
	}

	private void createInputField() {
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
									ExpressionValueString exprValue = new ExpressionValueString(value);
									addExpression(exprValue);
									window.close();
								}
							}
						});
						stringInputWindow.showCentered();
					}
				});

		inputButton.setWidth("100%");
		GridLayout expLayout = new GridLayout(1, 1);
		expLayout.setWidth("100%");
		expLayout.addComponent(inputButton);
		addComponent(expLayout);
		setComponentAlignment(expLayout, Alignment.MIDDLE_CENTER);
	}

	private Button createButton(String caption, ClickListener listener) {
		Button button = new Button(caption, listener);
		button.setWidth(BUTTON_WIDTH);
		button.addStyleName("v-expression-button-selector");
		return button;
	}

}
