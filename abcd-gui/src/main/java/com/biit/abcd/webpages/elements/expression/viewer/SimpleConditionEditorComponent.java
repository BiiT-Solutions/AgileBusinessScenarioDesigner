package com.biit.abcd.webpages.elements.expression.viewer;


public class SimpleConditionEditorComponent extends SimpleExpressionEditorComponent {
	private static final long serialVersionUID = -9034167340581462576L;

	public SimpleConditionEditorComponent() {
		super();
	}
	
	@Override
	protected ConditionExpressionViewer createExpressionViewer() {
		ConditionExpressionViewer expressionViewer = new ConditionExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

}
