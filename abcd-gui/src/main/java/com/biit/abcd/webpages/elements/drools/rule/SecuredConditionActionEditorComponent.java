package com.biit.abcd.webpages.elements.drools.rule;

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
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionViewer;
import com.biit.abcd.webpages.elements.expression.viewer.SecuredExpressionViewer;

public class SecuredConditionActionEditorComponent extends ConditionActionEditorComponent {
	private static final long serialVersionUID = -1507268124647978722L;

	public SecuredConditionActionEditorComponent() {
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
	protected ExpressionViewer createExpressionViewer() {
		ExpressionViewer expressionViewer = new SecuredExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

	@Override
	public void updateSelectionStyles() {
		if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.updateSelectionStyles();
		} else {
			getActionViewer().addStyleName("expression-unselected");
			getConditionViewer().addStyleName("expression-unselected");
		}
	}

	@Override
	protected void enableAssignOperator() {
		if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.enableAssignOperator();
		} else {
			enableAssignOperator(false);
		}
	}
}
