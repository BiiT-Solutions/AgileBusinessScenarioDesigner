package com.biit.abcd.webpages.elements.expressionviewer;

import java.text.ParseException;

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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AccordionMultiple;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class TabOperatorLayout extends TabLayout {
	private static final long serialVersionUID = -6980953550185164306L;
	private static final int GRID_COLUMNS = 3;

	public TabOperatorLayout() {
		AccordionMultiple accordion = new AccordionMultiple();

		VerticalLayout matLayout = new VerticalLayout();
		GridLayout inputLayout = new GridLayout(1, 1);
		inputLayout.setWidth("100%");
		createInputField(inputLayout);

		GridLayout operatorLayout = new GridLayout(GRID_COLUMNS, 4);
		operatorLayout.setWidth("100%");

		createMathOperators(operatorLayout);
		createMathFunctionsOperators(operatorLayout);
		createBaseTab(operatorLayout);

		matLayout.addComponent(inputLayout);
		matLayout.addComponent(operatorLayout);

		accordion.addTab(matLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_MATH), true);

		GridLayout logicalLayout = new GridLayout(GRID_COLUMNS, 4);
		logicalLayout.setWidth("100%");
		createLogicalOperators(logicalLayout);
		createLogicalFunctionsOperators(logicalLayout);

		accordion.addTab(logicalLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_LOGICAL), true);
		addComponent(accordion);
		setComponentAlignment(accordion, Alignment.MIDDLE_CENTER);
		this.setMargin(false);
	}

	private void createMathOperators(GridLayout layout) {
		Button assignButton = createButton("=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.ASSIGNATION);
			}
		});

		Button plusButton = createButton("+", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.PLUS);
			}
		});

		Button minusButton = createButton("-", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.MINUS);
			}
		});

		Button multButton = createButton("*", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.MULTIPLICATION);
			}
		});

		Button divButton = createButton("/", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.DIVISION);
			}
		});

		Button moduleButton = createButton("%", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.MODULE);
			}
		});

		Button potButton = createButton("x\u207F", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addMathematicalExpression(AvailableOperator.POW);
			}
		});

		assignButton.setWidth("100%");
		layout.addComponent(assignButton, 0, 0, GRID_COLUMNS - 1, 0);
		layout.addComponent(plusButton);
		layout.addComponent(minusButton);
		layout.addComponent(multButton);
		layout.addComponent(divButton);
		layout.addComponent(moduleButton);
		layout.addComponent(potButton);
	}

	private void createMathFunctionsOperators(AbstractLayout layout) {

		Button maxButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MAX),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.MAX);
					}
				});

		Button minimumButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MIN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.MIN);
					}
				});

		Button absoluteButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ABS),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.ABS);
					}
				});

		Button sqrtButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_SQRT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.SQRT);
					}
				});

		Button roundButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ROUND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.ROUND);
					}
				});

		Button averageButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_AVG),
				new ClickListener() {
					private static final long serialVersionUID = -1931977283394000885L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.AVG);
					}
				});

		Button pmtButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_PMT),
				new ClickListener() {
					private static final long serialVersionUID = 3549151891823532732L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.PMT);
					}
				});

		Button sumButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_SUM),
				new ClickListener() {
					private static final long serialVersionUID = -6814913505575268218L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.SUM);
					}
				});

		layout.addComponent(maxButton);
		layout.addComponent(minimumButton);
		layout.addComponent(absoluteButton);
		layout.addComponent(sqrtButton);
		layout.addComponent(roundButton);
		layout.addComponent(averageButton);
		layout.addComponent(pmtButton);
		layout.addComponent(sumButton);
	}

	private void createLogicalFunctionsOperators(AbstractLayout layout) {
		Button notButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.NOT);
					}
				});
		Button inButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_IN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.IN);
					}
				});

		Button betweenButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_BETWEEN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.BETWEEN);
					}
				});

		Button ifButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_IF),
				new ClickListener() {
					private static final long serialVersionUID = -1236836190814617814L;

					@Override
					public void buttonClick(ClickEvent event) {
						addFunctionExpression(AvailableFunction.IF);
					}
				});

		layout.addComponent(notButton);
		layout.addComponent(inButton);
		layout.addComponent(betweenButton);
		layout.addComponent(ifButton);
	}

	private void createBaseTab(AbstractLayout layout) {
		Button leftBracketButton = createButton("(", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				addSymbolExpression(AvailableSymbol.LEFT_BRACKET);
			}
		});

		Button rightBracketButton = createButton(")", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				addSymbolExpression(AvailableSymbol.RIGHT_BRACKET);
			}
		});

		Button commaButton = createButton(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addSymbolExpression(AvailableSymbol.COMMA);
			}
		});
		layout.addComponent(leftBracketButton);
		layout.addComponent(rightBracketButton);
		layout.addComponent(commaButton);
	}

	private void createLogicalOperators(AbstractLayout layout) {
		Button andButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_AND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addLogicalExpression(AvailableOperator.AND);
					}
				});

		Button orButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_OR),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						addLogicalExpression(AvailableOperator.OR);
					}
				});

		Button greaterThanButton = createButton(">", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.GREATER_THAN);
			}
		});

		Button greaterEqualsButton = createButton("\u2265", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.GREATER_EQUALS);
			}
		});

		Button lessThanButton = createButton("<", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.LESS_THAN);
			}
		});

		Button lessEqualsButton = createButton("\u2264", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.LESS_EQUALS);
			}
		});

		Button equalsButton = createButton("==", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.EQUALS);
			}
		});

		Button distinctButton = createButton("!=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				addLogicalExpression(AvailableOperator.NOT_EQUALS);
			}
		});

		layout.addComponent(andButton);
		layout.addComponent(orButton);
		layout.addComponent(greaterThanButton);
		layout.addComponent(greaterEqualsButton);
		layout.addComponent(lessThanButton);
		layout.addComponent(lessEqualsButton);
		layout.addComponent(equalsButton);
		layout.addComponent(distinctButton);
	}

	private void createInputField(AbstractLayout layout) {
		Button inputButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_INPUT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						StringInputWindow stringInputWindow = new StringInputWindow();
						stringInputWindow.setCaption(ServerTranslate
								.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_CAPTION));
						stringInputWindow.addAcceptActionListener(new AcceptActionListener() {
							@Override
							public void acceptAction(AcceptCancelWindow window) {
								String value = ((StringInputWindow) window).getValue();
								if ((value == null) || value.isEmpty()) {
									MessageManager.showError(ServerTranslate
											.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
								} else {
									// It is a number.
									try {
										switch (((StringInputWindow) window).getFormat()) {
										case NUMBER:
											try {
												Double valueAsDouble = Double.parseDouble(value);
												ExpressionValueNumber exprValueNumber = new ExpressionValueNumber(
														valueAsDouble);
												addExpression(exprValueNumber);
												window.close();
											} catch (NumberFormatException nfe) {
												throw new NotValidExpressionValue("Value '" + value
														+ "' is not a number!");
											}
											break;
										case DATE:
											try {
												ExpressionValueTimestamp exprValueDate;
												exprValueDate = new ExpressionValueTimestamp(value);
												addExpression(exprValueDate);
												window.close();
											} catch (ParseException e) {
												throw new NotValidExpressionValue("Value '" + value
														+ "' is not a number!");
											}
											break;
										case POSTAL_CODE:
											ExpressionValuePostalCode exprValuePostCode = new ExpressionValuePostalCode(
													value);
											addExpression(exprValuePostCode);
											window.close();
											break;
										case TEXT:
											ExpressionValueString exprValueString = new ExpressionValueString(value);
											addExpression(exprValueString);
											window.close();
											break;
										}
									} catch (NotValidExpressionValue e1) {
										MessageManager.showError(LanguageCodes.ERROR_INVALID_VALUE);
									}
								}
							}
						});
						stringInputWindow.showCentered();
					}
				});

		inputButton.setWidth("100%");
		layout.setWidth("100%");
		layout.addComponent(inputButton);
	}

	private Button createButton(String caption, ClickListener listener) {
		Button button = new Button(caption, listener);
		button.setWidth(BUTTON_WIDTH);
		button.addStyleName("v-expression-button-selector");
		return button;
	}

	private void addMathematicalExpression(AvailableOperator operator) {
		ExpressionOperatorMath exprValue = new ExpressionOperatorMath();
		try {
			exprValue.setValue(operator);
			addExpression(exprValue);
		} catch (NotValidOperatorInExpression e) {
		}
	}

	private void addFunctionExpression(AvailableFunction function) {
		ExpressionFunction exprValue = new ExpressionFunction();
		exprValue.setValue(function);
		addExpression(exprValue);
	}

	private void addSymbolExpression(AvailableSymbol symbol) {
		ExpressionSymbol exprValue = new ExpressionSymbol();
		exprValue.setValue(symbol);
		addExpression(exprValue);
	}

	private void addLogicalExpression(AvailableOperator operator) {
		ExpressionOperatorLogic exprValue = new ExpressionOperatorLogic();
		try {
			exprValue.setValue(operator);
			addExpression(exprValue);
		} catch (NotValidOperatorInExpression e) {

		}
	}
}
