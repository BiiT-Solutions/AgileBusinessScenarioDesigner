package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.vaadin.ui.HorizontalLayout;

/**
 * Checks user permissions to modify an expression.
 * 
 */
public class SecuredExpressionViewer extends ExpressionViewer {
	private static final long serialVersionUID = 6372099281146635159L;

	/**
	 * An expression is editable if it is and user has permissions.
	 */
	@Override
	protected boolean isExpressionEditable(Expression expression) {
		return expression.isEditable()
				&& !AbcdFormAuthorizationService.getInstance().isFormReadOnly(
						UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser());
	}

	/**
	 * if disabled, no expression can be selected.
	 */
	@Override
	protected void updateExpressionSelectionStyles() {
		if (!AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser())) {
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
