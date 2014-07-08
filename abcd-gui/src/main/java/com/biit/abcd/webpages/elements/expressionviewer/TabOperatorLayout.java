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
//		addComponent(inputLayout);
//		setComponentAlignment(inputLayout, Alignment.MIDDLE_CENTER);

		GridLayout operatorLayout = new GridLayout(GRID_COLUMNS, 4);
		operatorLayout.setWidth("100%");

		createMathOperators(operatorLayout);
		createMathFunctionsOperators(operatorLayout);
		createBaseTab(operatorLayout);
		
		matLayout.addComponent(inputLayout);
		matLayout.addComponent(operatorLayout);

//		addComponent(operatorLayout);
//		setComponentAlignment(operatorLayout, Alignment.MIDDLE_CENTER);
		accordion.addTab(matLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_MATH), true);

		GridLayout logicalLayout = new GridLayout(GRID_COLUMNS, 4);
		logicalLayout.setWidth("100%");
		createLogicalOperators(logicalLayout);
		createLogicalFunctionsOperators(logicalLayout);

//		addComponent(logicalLayout);
//		setComponentAlignment(logicalLayout, Alignment.MIDDLE_CENTER);
		
		accordion.addTab(logicalLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_LOGICAL), true);
		addComponent(accordion);
		setComponentAlignment(accordion, Alignment.MIDDLE_CENTER);
		this.setMargin(false);
	}

	private void createMathOperators(AbstractLayout layout) {
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

		layout.addComponent(assignButton);
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

		layout.addComponent(maxButton);
		layout.addComponent(minimumButton);
		layout.addComponent(absoluteButton);
		layout.addComponent(sqrtButton);
		layout.addComponent(roundButton);
	}

	private void createLogicalFunctionsOperators(AbstractLayout layout) {
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

		layout.addComponent(notButton);
		layout.addComponent(inButton);
		layout.addComponent(betweenButton);
	}

	private void createBaseTab(AbstractLayout layout) {
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

		Button commaButton = createButton(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.COMMA);
				addExpression(exprValue);
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
		layout.setWidth("100%");
		layout.addComponent(inputButton);
	}

	private Button createButton(String caption, ClickListener listener) {
		Button button = new Button(caption, listener);
		button.setWidth(BUTTON_WIDTH);
		button.addStyleName("v-expression-button-selector");
		return button;
	}

}
