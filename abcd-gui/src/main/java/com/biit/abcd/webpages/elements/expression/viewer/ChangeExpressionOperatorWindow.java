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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ChangeExpressionOperatorWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -6909710962670541421L;
	private ComboBox operatorComboBox;

	public ChangeExpressionOperatorWindow(Expression expression) {
		super();
		setWidth("15%");
		setHeight("20%");
		setContent(generateContent(expression));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHANGE_OPERATOR_WINDOW_CAPTION));
	}

	public Component generateContent(Expression expression) {
		VerticalLayout layout = new VerticalLayout();

		operatorComboBox = new ComboBox(ServerTranslate.translate(LanguageCodes.EXPRESSION_CHANGE_OPERATOR_WINDOW_COMBOBOX));
		operatorComboBox.setNullSelectionAllowed(false);

		if (expression instanceof ExpressionOperator) {
			for (AvailableOperator operator : ((ExpressionOperator) expression).getAcceptedValues()) {
				if (!operator.equals(AvailableOperator.NULL)) {
					operatorComboBox.addItem(operator);
				}
			}
			operatorComboBox.setValue(((ExpressionOperator) expression).getValue());
		}

		operatorComboBox.setSizeFull();
		layout.addComponent(operatorComboBox);
		layout.setSizeFull();
		layout.setMargin(false);
		return layout;
	}

	public AvailableOperator getOperator() {
		return (AvailableOperator) operatorComboBox.getValue();
	}
}
