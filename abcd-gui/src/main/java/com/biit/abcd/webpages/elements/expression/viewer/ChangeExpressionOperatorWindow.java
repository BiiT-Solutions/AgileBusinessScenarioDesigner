package com.biit.abcd.webpages.elements.expression.viewer;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChangeExpressionOperatorWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -6909710962670541421L;
	private ComboBox operatorComboBox;

	public ChangeExpressionOperatorWindow(Expression expression) {
		super();
		setWidth("15%");
		setHeight("20%");
		setContent(generateContent(expression));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHANGE_OPERATOR_WINDOW_CAPTION));
	}

	public Component generateContent(Expression expression) {
		VerticalLayout layout = new VerticalLayout();

		operatorComboBox = new ComboBox(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHANGE_OPERATOR_WINDOW_COMBOBOX));
		operatorComboBox.setNullSelectionAllowed(false);

		if (expression instanceof ExpressionOperator) {
			for (AvailableOperator operator : ((ExpressionOperator) expression).getAcceptedValues()) {
				if (!operator.equals(AvailableOperator.NULL)) {
					operatorComboBox.addItem(operator);
				}
			}
			operatorComboBox.setValue(((ExpressionOperator) expression).getValue());
		}

		operatorComboBox.setSizeFull();
		layout.addComponent(operatorComboBox);
		layout.setSizeFull();
		layout.setMargin(false);
		return layout;
	}

	public AvailableOperator getOperator() {
		return (AvailableOperator) operatorComboBox.getValue();
	}
}
