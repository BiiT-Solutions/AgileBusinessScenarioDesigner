package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExprOpMath;
import com.biit.abcd.persistence.entity.expressions.ExprOpValue;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
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
		Button andButton = new Button("And", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		andButton.setWidth(buttonWidth);

		Button orButton = new Button("Or", new ClickListener() {
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

		addTab(exprLogicLayout, "TODO - Logical Operator", true);
	}

	private void createMathTab() {
		Button plusButton = new Button("+", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				// ExprAtomicMath plusExpression = new ExprAtomicMath();
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

		addTab(exprAtomicMathLayout, "TODO - Math Operators", true);
	}

	private void createFunctionTab() {
		Button notButton = new Button("Not", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		notButton.setWidth(buttonWidth);

		Button maxButton = new Button("Max", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		maxButton.setWidth(buttonWidth);

		Button minimumButton = new Button("Min", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		minimumButton.setWidth(buttonWidth);

		Button absoluteButton = new Button("Abs", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		absoluteButton.setWidth(buttonWidth);

		Button sqrtButton = new Button("\u221A", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		sqrtButton.setWidth(buttonWidth);

		Button roundButton = new Button("Round", new ClickListener() {
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

		addTab(exprFuncLayout, "TODO - Functions", true);
	}

	private void createBaseTab() {
		Button leftBracketButton = new Button("(", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		leftBracketButton.setWidth(buttonWidth);

		Button rightBracketButton = new Button(")", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		rightBracketButton.setWidth(buttonWidth);

		Button assignButton = new Button("=", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		assignButton.setWidth(buttonWidth);

		GridLayout exprBaseLayout = new GridLayout(GRID_COLUMNS, 4);
		exprBaseLayout.setWidth(null);
		exprBaseLayout.addComponent(leftBracketButton);
		exprBaseLayout.addComponent(rightBracketButton);
		exprBaseLayout.addComponent(assignButton);

		addTab(exprBaseLayout, "TODO - Generic", true);
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

		GridLayout exprControlsLayout = new GridLayout(GRID_COLUMNS, 4);
		exprControlsLayout.setWidth(null);
		exprControlsLayout.addComponent(deleteButton);
		exprControlsLayout.addComponent(newLineButton);

		addTab(exprControlsLayout, "TODO - Controls", true);
	}

	private void createFormTab() {
		Button globalConstantButton = new Button("Global", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		globalConstantButton.setWidth(formButtonWidth);

		Button FormVariabletButton = new Button("Variable", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		FormVariabletButton.setWidth(formButtonWidth);

		GridLayout exprFormLayout = new GridLayout(1, 4);
		exprFormLayout.setWidth(null);
		exprFormLayout.addComponent(globalConstantButton);
		exprFormLayout.addComponent(FormVariabletButton);

		addTab(exprFormLayout, "TODO - Form Values", true);
	}

	private void addExpression(ExprBasic expression) {
		formExpression.addExpression(expression);
		firePropertyUpdateListener(formExpression);
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
