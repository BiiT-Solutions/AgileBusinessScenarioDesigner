package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionEditorComponent;
import com.vaadin.ui.VerticalLayout;

public class ExpressionEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private ExpressionEditorComponent expressionEditorComponent;

	public ExpressionEditor() {
		super();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		// rootLayout.addComponent(new FormulaEditor());
		expressionEditorComponent = new ExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();
		expressionEditorComponent.addWhenExpression();
		expressionEditorComponent.addThenExpression();
		rootLayout.addComponent(expressionEditorComponent);

		getWorkingAreaLayout().addComponent(rootLayout);

	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}
}
