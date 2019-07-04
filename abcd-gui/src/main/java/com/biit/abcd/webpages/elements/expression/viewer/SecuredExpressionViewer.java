package com.biit.abcd.webpages.elements.expression.viewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;

/**
 * Checks user permissions to modify an expression.
 * 
 */
public class SecuredExpressionViewer extends ExpressionViewer {
	private static final long serialVersionUID = 6372099281146635159L;

	private IAbcdFormAuthorizationService securityService;

	public SecuredExpressionViewer() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	/**
	 * An expression is editable if it is and user has permissions.
	 */
	@Override
	protected boolean isExpressionEditable(Expression expression) {
		return expression.isEditable()
				&& !securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
						UserSessionHandler.getUser());
	}

	/**
	 * if disabled, no expression can be selected.
	 */
	@Override
	protected void updateExpressionSelectionStyles() {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.updateExpressionSelectionStyles();
		} else {
			for (int i = 0; i < getRootLayout().getComponentCount(); i++) {
				if (getRootLayout().getComponent(i) instanceof HorizontalLayout) {
					HorizontalLayout lineLayout = (HorizontalLayout) getRootLayout().getComponent(i);
					for (int j = 0; j < lineLayout.getComponentCount(); j++) {
						if (lineLayout.getComponent(j) instanceof ExpressionElement) {
							if (!expressionOfElement.get(lineLayout.getComponent(j)).isEditable()) {
								lineLayout.getComponent(j).addStyleName("expression-disabled");
							}
						}
					}
				}
			}
		}
	}
}
