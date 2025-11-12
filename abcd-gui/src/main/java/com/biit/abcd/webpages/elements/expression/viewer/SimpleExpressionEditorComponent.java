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
