package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;

public class ExpressionOperatorsProperties extends PropertiesForClassComponent<FormExpression> {
	private static final long serialVersionUID = 7252467474326157874L;
	private static final int GRID_COLUMNS = 2;
	protected static final String buttonWidth = "80px";
	protected static final String formButtonWidth = "160px";
	private FormExpression formExpression;
	private Button deleteButton;

	public ExpressionOperatorsProperties() {
		super(FormExpression.class);
		createMathTab();
		createLogicalTab();
	}

	@Override
	protected void setElementAbstract(final FormExpression expression) {
		this.formExpression = expression;
	}

	private void createLogicalTab() {
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
		andButton.setWidth(buttonWidth);

		Button orButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_OR),
				new ClickListener() {
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
				ExpressionOperatorMath exprValue = new ExpressionOperatorMath();
				try {
					exprValue.setValue(AvailableOperator.PLUS);
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

		Button notButton = new Button(ServerTranslate.translate(LanguageCodes.EXPRESSION_BUTTON_NOT),
				new ClickListener() {
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
						ExpressionFunction exprValue = new ExpressionFunction();
						exprValue.setValue(AvailableFunction.MAX);
						addExpression(exprValue);
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

		Button commaButton = new Button(",", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionSymbol exprValue = new ExpressionSymbol();
				exprValue.setValue(AvailableSymbol.COMMA);
				addExpression(exprValue);
			}
		});
		commaButton.setWidth(buttonWidth);


		GridLayout exprAtomicMathLayout = new GridLayout(GRID_COLUMNS, 4);
		exprAtomicMathLayout.setWidth(null);
		exprAtomicMathLayout.addComponent(plusButton);
		exprAtomicMathLayout.addComponent(minusButton);
		exprAtomicMathLayout.addComponent(multButton);
		exprAtomicMathLayout.addComponent(divButton);
		exprAtomicMathLayout.addComponent(moduleButton);
		exprAtomicMathLayout.addComponent(potButton);
		exprAtomicMathLayout.addComponent(maxButton);
		exprAtomicMathLayout.addComponent(minimumButton);
		exprAtomicMathLayout.addComponent(absoluteButton);
		exprAtomicMathLayout.addComponent(sqrtButton);
		exprAtomicMathLayout.addComponent(roundButton);
		exprAtomicMathLayout.addComponent(commaButton);
		addTab(exprAtomicMathLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_MATH), true);

	}

	private void addExpression(Expression expression) {
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

}
