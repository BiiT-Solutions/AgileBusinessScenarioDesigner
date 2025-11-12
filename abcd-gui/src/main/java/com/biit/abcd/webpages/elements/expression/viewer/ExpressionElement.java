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

import java.sql.Timestamp;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.language.UserLocaleStringToDoubleConverter;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.utils.date.DateManager;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class ExpressionElement extends CssLayout {
	private static final long serialVersionUID = -1658928610594293678L;
	private static final String HEIGHT = "25px";
	private static final String STYLE = "v-expression-element";
	private Label elementName;

	public ExpressionElement(Expression expression, LayoutClickListener clickListener) {
		// Use language definition for dates.
		if ((expression instanceof ExpressionValueTimestamp) && !(expression instanceof ExpressionValueSystemDate)) {
			elementName = new Label(DateManager.convertDateToString((Timestamp) expression.getValue(),
					ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_DATE)));
			// Decimals are showed depending on the liferay's user configuration
		} else if (expression instanceof ExpressionValueNumber) {
			elementName = new Label();
			elementName.setConverter(new UserLocaleStringToDoubleConverter());
			ObjectProperty<Double> property = new ObjectProperty<Double>((Double) expression.getValue());
			elementName.setPropertyDataSource(property);
		} else {
			elementName = new Label(expression.getRepresentation(true));
		}

		this.setWidth(null);
		this.setHeight(HEIGHT);
		this.setStyleName(STYLE);

		addComponent(elementName);
		setImmediate(true);

		this.addLayoutClickListener(clickListener);
	}

	@Override
	public String toString() {
		return elementName.getCaption();
	}

}
