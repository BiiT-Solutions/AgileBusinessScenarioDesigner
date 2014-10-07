package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.vaadin.ui.VerticalLayout;

public class SimpleExpressionEditorComponent extends ExpressionEditorComponent {
	private static final long serialVersionUID = -9034167340581462576L;
	private ExpressionViewer expressionViewer;

	public SimpleExpressionEditorComponent() {
		super();
	}

	protected ExpressionViewer createExpressionViewer() {
		ExpressionViewer expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

	@Override
	public VerticalLayout createViewersLayout() {
		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setMargin(false);
		viewLayout.setSpacing(false);

		expressionViewer = createExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);

		viewLayout.addComponent(expressionViewer);
		return viewLayout;
	}

	@Override
	public void updateSelectionStyles() {
		if (getSelectedViewer().getExpressions() == null) {
			getSelectedViewer().addStyleName("expression-unselected");
		} else {
			getSelectedViewer().removeStyleName("expression-unselected");
		}
	}

	@Override
	public ExpressionViewer getSelectedViewer() {
		return expressionViewer;
	}

	public void refreshExpressionEditor(ExpressionChain selectedExpression) {
		if (getSelectedViewer() != null) {
			getSelectedViewer().removeAllComponents();
			if (selectedExpression != null) {
				// Add table rows.
				getSelectedViewer().updateExpression(selectedExpression);
			} else {
				getSelectedViewer().updateExpression(null);
			}
		}
		updateSelectionStyles();
	}

	public void clear() {
		getSelectedViewer().clearExpression();
	}
}
