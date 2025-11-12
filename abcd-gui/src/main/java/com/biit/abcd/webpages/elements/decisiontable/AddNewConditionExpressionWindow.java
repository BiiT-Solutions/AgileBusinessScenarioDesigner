package com.biit.abcd.webpages.elements.decisiontable;

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
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleConditionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleExpressionEditorComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNewConditionExpressionWindow extends AddNewAnswerExpressionWindow {
	private static final long serialVersionUID = -6236016121358126850L;

	public AddNewConditionExpressionWindow(ExpressionValueTreeObjectReference reference, ExpressionChain expressionChain) {
		super(reference, expressionChain);
	}

	@Override
	public Component generateExpression() {
		setWidth("90%");
		setHeight("90%");
		VerticalLayout layout = new VerticalLayout();

		// Create content
		setExpressionEditorComponent(new SimpleConditionEditorComponent());
		getExpressionEditorComponent().setSizeFull();
		// The first expression of the expression chain must be not editable
		((SimpleExpressionEditorComponent) getExpressionEditorComponent())
				.refreshExpressionEditor(expressionChain);

		layout.addComponent(getExpressionEditorComponent());
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}
}
