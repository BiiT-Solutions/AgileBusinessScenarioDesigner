package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;

public class SecuredSimpleExpressionEditorComponent extends SimpleExpressionEditorComponent {
	private static final long serialVersionUID = 8674578404864292066L;

	private IAbcdFormAuthorizationService securityService;

	public SecuredSimpleExpressionEditorComponent() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	protected void addKeyController() {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.addKeyController();
		}
	}

	@Override
	protected void addElementToView(Object newElement) {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.addElementToView(newElement);
		}
	}

	@Override
	public void updateSelectionStyles() {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
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
