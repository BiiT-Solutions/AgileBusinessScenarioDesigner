package com.biit.abcd.webpages.elements.droolsrule;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionViewer;
import com.biit.abcd.webpages.elements.expressionviewer.SecuredExpressionViewer;

public class SecuredConditionActionEditorComponent extends ConditionActionEditorComponent {
	private static final long serialVersionUID = -1507268124647978722L;

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
	protected ExpressionViewer createExpressionViewer() {
		ExpressionViewer expressionViewer = new SecuredExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

	@Override
	public void updateSelectionStyles() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
			super.updateSelectionStyles();
		} else {
			getActionViewer().addStyleName("expression-unselected");
			getConditionViewer().addStyleName("expression-unselected");
		}
	}

	@Override
	protected void enableAssignOperator() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
			super.enableAssignOperator();
		} else {
			enableAssignOperator(false);
		}
	}
}
