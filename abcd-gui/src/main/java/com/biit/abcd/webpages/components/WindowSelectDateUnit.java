package com.biit.abcd.webpages.components;

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
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Selects if a date question expression is defined as year, month, or days.
 */
public class WindowSelectDateUnit extends AcceptCancelWindow {
	private static final long serialVersionUID = 361486551550136464L;
	private static final String width = "300px";
	private static final String height = "180px";

	private ComboBox unitSelector;

	public WindowSelectDateUnit(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public QuestionDateUnit getValue() {
		return (QuestionDateUnit) unitSelector.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		unitSelector = new ComboBox(inputFieldCaption);
		unitSelector.setNullSelectionAllowed(false);

		for (QuestionDateUnit unit : QuestionDateUnit.values()) {
			unitSelector.addItem(unit);
			String caption = "";
			switch (unit) {
			case DAYS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_DAY);
				break;
			case MONTHS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_MONTH);
				break;
			case YEARS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_YEAR);
				break;
			case DATE:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_DATE);
				break;
			case ABSOLUTE_DAYS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_DAY_FROM_ORIGIN);
				break;
			case ABSOLUTE_MONTHS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_MONTH_FROM_ORIGIN);
				break;
			case ABSOLUTE_YEARS:
				caption = ServerTranslate.translate(LanguageCodes.EXPRESSION_DATE_YEAR_FROM_ORIGIN);
				break;
			}
			unitSelector.setItemCaption(unit, caption);
			unitSelector.setValue(unit);
		}

		unitSelector.focus();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(unitSelector);
		rootLayout.setComponentAlignment(unitSelector, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}

	public void setValue(QuestionDateUnit value) {
		unitSelector.setValue(value);
	}
}
