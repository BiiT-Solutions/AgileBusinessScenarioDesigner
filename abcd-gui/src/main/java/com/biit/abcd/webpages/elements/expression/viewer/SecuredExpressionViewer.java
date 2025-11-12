package com.biit.abcd.webpages.elements.expression.viewer;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
