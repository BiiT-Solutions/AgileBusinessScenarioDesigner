package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleConditionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleExpressionEditorComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewConditionExpressionWindow extends AddNewAnswerExpressionWindow {
	private static final long serialVersionUID = -6236016121358126850L;

	public AddNewConditionExpressionWindow(ExpressionValueTreeObjectReference reference, ExpressionChain expressionChain) {
		super(reference, expressionChain);
	}

	@Override
	public Component generateExpression() {
		setWidth("90%");
		setHeight("90%");
		VerticalLayout layout = new VerticalLayout();

		// Create content
		setExpressionEditorComponent(new SimpleConditionEditorComponent());
		getExpressionEditorComponent().setSizeFull();
		// The first expression of the expression chain must be not editable
		((SimpleExpressionEditorComponent) getExpressionEditorComponent())
				.refreshExpressionEditor(expressionChain);

		layout.addComponent(getExpressionEditorComponent());
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}
}