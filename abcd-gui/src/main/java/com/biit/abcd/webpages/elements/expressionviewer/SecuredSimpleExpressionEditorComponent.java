package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.security.AbcdFormAuthorizationService;

public class SecuredSimpleExpressionEditorComponent extends SimpleExpressionEditorComponent {
	private static final long serialVersionUID = 8674578404864292066L;

	@Override
	protected void addKeyController() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
			super.addKeyController();
		}
	}

	@Override
	protected void addElementToView(Object newElement) {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
			super.addElementToView(newElement);
		}
	}

	@Override
	public void updateSelectionStyles() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
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
