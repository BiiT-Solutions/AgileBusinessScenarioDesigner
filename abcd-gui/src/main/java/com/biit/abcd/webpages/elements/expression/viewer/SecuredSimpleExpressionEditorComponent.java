package com.biit.abcd.webpages.elements.expression.viewer;

import com.biit.abcd.authentication.UserSessionHandler;

public class SecuredSimpleExpressionEditorComponent extends SimpleExpressionEditorComponent {
	private static final long serialVersionUID = 8674578404864292066L;

	public SecuredSimpleExpressionEditorComponent() {
		super();
	}

	@Override
	protected void addKeyController() {
		if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.addKeyController();
		}
	}

	@Override
	protected void addElementToView(Object newElement) {
		if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.addElementToView(newElement);
		}
	}

	@Override
	public void updateSelectionStyles() {
		if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.updateSelectionStyles();
		} else {
			getSelectedViewer().addStyleName("expression-unselected");
		}
	}

	@Override
	protected ExpressionViewer createExpressionViewer() {
		ExpressionViewer expressionViewer = new SecuredExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

}
