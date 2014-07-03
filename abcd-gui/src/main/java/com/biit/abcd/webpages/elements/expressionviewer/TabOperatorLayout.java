package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;

public class TabOperatorLayout extends TabLayout {
	private static final long serialVersionUID = -6980953550185164306L;
	private static final int GRID_COLUMNS = 3;

	public TabOperatorLayout() {
		createMathOperators();
		createFunctionsOperators();
		createBaseTab();
		createLogicalOperators();

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

		GridLayout exprAtomicMathLayout = new GridLayout(GRID_COLUMNS, 4);
		exprAtomicMathLayout.setWidth("100%");
		exprAtomicMathLayout.addComponent(plusButton);
		exprAtomicMathLayout.addComponent(minusButton);
		exprAtomicMathLayout.addComponent(multButton);
		exprAtomicMathLayout.addComponent(divButton);
		exprAtomicMathLayout.addComponent(moduleButton);
		exprAtomicMathLayout.addComponent(potButton);
		addComponent(exprAtomicMathLayout);
		setComponentAlignment(exprAtomicMathLayout, Alignment.MIDDLE_CENTER);

	}

	private void createFunctionsOperators() {
		Button notButton = createButton(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});

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

		Button commaButton = createButton(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.COMMA);
				addExpression(exprValue);
			}
		});

		GridLayout exprFunctionLayout = new GridLayout(GRID_COLUMNS, 4);
		exprFunctionLayout.setWidth("100%");
		exprFunctionLayout.addComponent(notButton);
		exprFunctionLayout.addComponent(maxButton);
		exprFunctionLayout.addComponent(minimumButton);
		exprFunctionLayout.addComponent(absoluteButton);
		exprFunctionLayout.addComponent(sqrtButton);
		exprFunctionLayout.addComponent(roundButton);
		exprFunctionLayout.addComponent(commaButton);
		addComponent(exprFunctionLayout);
		setComponentAlignment(exprFunctionLayout, Alignment.MIDDLE_CENTER);
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

		GridLayout exprBaseLayout = new GridLayout(GRID_COLUMNS, 4);
		exprBaseLayout.setWidth("100%");
		exprBaseLayout.addComponent(leftBracketButton);
		exprBaseLayout.addComponent(rightBracketButton);
		exprBaseLayout.addComponent(assignButton);

		addComponent(exprBaseLayout);
		setComponentAlignment(exprBaseLayout, Alignment.MIDDLE_CENTER);
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

		GridLayout exprLogicLayout = new GridLayout(GRID_COLUMNS, 4);
		exprLogicLayout.setWidth("100%");
		exprLogicLayout.addComponent(andButton);
		exprLogicLayout.addComponent(orButton);
		exprLogicLayout.addComponent(greaterThanButton);
		exprLogicLayout.addComponent(greaterEqualsButton);
		exprLogicLayout.addComponent(lessThanButton);
		exprLogicLayout.addComponent(lessEqualsButton);
		exprLogicLayout.addComponent(equalsButton);
		exprLogicLayout.addComponent(distinctButton);

		addComponent(exprLogicLayout);
		setComponentAlignment(exprLogicLayout, Alignment.MIDDLE_CENTER);
	}

	private Button createButton(String caption, ClickListener listener) {
		Button button = new Button(caption, listener);
		button.setWidth(BUTTON_WIDTH);
		button.addStyleName("v-expression-button-selector");
		return button;
	}

}
