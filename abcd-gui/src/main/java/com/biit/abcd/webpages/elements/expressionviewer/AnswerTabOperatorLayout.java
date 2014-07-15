package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
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

public class AnswerTabOperatorLayout extends TabLayout {
	private static final long serialVersionUID = -6980953550185164306L;
	private static final int GRID_COLUMNS = 3;

	public AnswerTabOperatorLayout() {
		AccordionMultiple accordion = new AccordionMultiple();

		VerticalLayout matLayout = new VerticalLayout();
		GridLayout inputLayout = new GridLayout(1, 1);
		inputLayout.setWidth("100%");
		createInputField(inputLayout);

		GridLayout operatorLayout = new GridLayout(GRID_COLUMNS, 4);
		operatorLayout.setWidth("100%");

		createBaseTab(operatorLayout);

		matLayout.addComponent(inputLayout);
		matLayout.addComponent(operatorLayout);

		accordion.addTab(matLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_MATH), true);

		GridLayout logicalLayout = new GridLayout(GRID_COLUMNS, 4);
		logicalLayout.setWidth("100%");
		createLogicalFunctionsOperators(logicalLayout);

		accordion.addTab(logicalLayout, ServerTranslate.translate(LanguageCodes.EXPRESSION_PROPERTIES_LOGICAL), true);
		addComponent(accordion);
		setComponentAlignment(accordion, Alignment.MIDDLE_CENTER);
		this.setMargin(false);
	}

	private void createLogicalFunctionsOperators(AbstractLayout layout) {
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

		layout.addComponent(inButton);
		layout.addComponent(betweenButton);
	}

	private void createBaseTab(AbstractLayout layout) {
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
		layout.addComponent(rightBracketButton);
		layout.addComponent(commaButton);
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
				stringInputWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						String value = ((StringInputWindow) window).getValue();
						if ((value == null) || value.isEmpty()) {
							MessageManager.showError(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_ERROR_INCORRECT_INPUT_VALUE));
						} else {
							// Is a number.
							try {
								Double valueAsDouble = Double.parseDouble(value);
								ExpressionValueNumber exprValue = new ExpressionValueNumber(valueAsDouble);
								addExpression(exprValue);
								// Is a string.
							} catch (NumberFormatException nfe) {
								ExpressionValueString exprValue = new ExpressionValueString(value);
								addExpression(exprValue);
							}
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
}
