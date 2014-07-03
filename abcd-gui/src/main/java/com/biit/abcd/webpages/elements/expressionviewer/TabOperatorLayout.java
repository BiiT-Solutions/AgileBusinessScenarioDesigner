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
	private static final int GRID_COLUMNS = 2;

	public TabOperatorLayout() {
		createMathOperators();
		createFunctionsOperators();
		createBaseTab();
		createLogicalOperators();

	}

	private void createMathOperators() {
		Button plusButton = new Button("+", new ClickListener() {
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
		plusButton.setWidth(BUTTON_WIDTH);

		Button minusButton = new Button("-", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		minusButton.setWidth(BUTTON_WIDTH);

		Button multButton = new Button("*", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		multButton.setWidth(BUTTON_WIDTH);

		Button divButton = new Button("/", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		divButton.setWidth(BUTTON_WIDTH);

		Button moduleButton = new Button("%", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		moduleButton.setWidth(BUTTON_WIDTH);

		Button potButton = new Button("x\u207F", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		potButton.setWidth(BUTTON_WIDTH);

		GridLayout exprAtomicMathLayout = new GridLayout(GRID_COLUMNS, 4);
		exprAtomicMathLayout.setWidth(null);
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
		Button notButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		notButton.setWidth(BUTTON_WIDTH);

		Button maxButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MAX),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionFunction exprValue = new ExpressionFunction();
						exprValue.setValue(AvailableFunction.MAX);
						addExpression(exprValue);
					}
				});
		maxButton.setWidth(BUTTON_WIDTH);

		Button minimumButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_MIN),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		minimumButton.setWidth(BUTTON_WIDTH);

		Button absoluteButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ABS),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		absoluteButton.setWidth(BUTTON_WIDTH);

		Button sqrtButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_SQRT),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		sqrtButton.setWidth(BUTTON_WIDTH);

		Button roundButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_ROUND),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		roundButton.setWidth(BUTTON_WIDTH);

		Button commaButton = new Button(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.COMMA);
				addExpression(exprValue);
			}
		});
		commaButton.setWidth(BUTTON_WIDTH);

		GridLayout exprFunctionLayout = new GridLayout(GRID_COLUMNS, 4);
		exprFunctionLayout.setWidth(null);
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
		Button leftBracketButton = new Button("(", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.LEFT_BRACKET);
				addExpression(exprValue);
			}
		});
		leftBracketButton.setWidth(BUTTON_WIDTH);

		Button rightBracketButton = new Button(")", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.RIGHT_BRACKET);
				addExpression(exprValue);
			}
		});
		rightBracketButton.setWidth(BUTTON_WIDTH);

		Button assignButton = new Button("=", new ClickListener() {
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
		assignButton.setWidth(BUTTON_WIDTH);

		GridLayout exprBaseLayout = new GridLayout(GRID_COLUMNS, 4);
		exprBaseLayout.setWidth(null);
		exprBaseLayout.addComponent(leftBracketButton);
		exprBaseLayout.addComponent(rightBracketButton);
		exprBaseLayout.addComponent(assignButton);

		addComponent(exprBaseLayout);
		setComponentAlignment(exprBaseLayout, Alignment.MIDDLE_CENTER);
	}

	private void createLogicalOperators() {
		Button andButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_AND),
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
		andButton.setWidth(BUTTON_WIDTH);

		Button orButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_OR),
				new ClickListener() {
					private static final long serialVersionUID = -3339234972234970277L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		orButton.setWidth(BUTTON_WIDTH);

		Button greaterThanButton = new Button(">", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		greaterThanButton.setWidth(BUTTON_WIDTH);

		Button greaterEqualsButton = new Button("\u2265", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		greaterEqualsButton.setWidth(BUTTON_WIDTH);

		Button lessThanButton = new Button("<", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		lessThanButton.setWidth(BUTTON_WIDTH);

		Button lessEqualsButton = new Button("\u2264", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		lessEqualsButton.setWidth(BUTTON_WIDTH);

		Button equalsButton = new Button("==", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		equalsButton.setWidth(BUTTON_WIDTH);

		Button distinctButton = new Button("!=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		distinctButton.setWidth(BUTTON_WIDTH);

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

		addComponent(exprLogicLayout);
		setComponentAlignment(exprLogicLayout, Alignment.MIDDLE_CENTER);
	}

}
