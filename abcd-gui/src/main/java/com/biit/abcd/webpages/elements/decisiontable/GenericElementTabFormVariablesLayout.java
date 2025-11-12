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

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.webpages.components.TreeObjectTableMultiSelect;
import com.biit.abcd.webpages.elements.expression.viewer.TabFormVariablesLayout;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class GenericElementTabFormVariablesLayout extends TabFormVariablesLayout {
	private static final long serialVersionUID = 703942162061962183L;

	public GenericElementTabFormVariablesLayout() {
		super();
		createFormVariablesElements();
	}

	/**
	 * We can select more than one element, then we add expressions separated by
	 * commas. If we select a date question or variable, then we also must
	 * select the unit for the date expression.
	 */
	private void createFormVariablesElements() {
		// Form elements list
		createFormElementsComponent();
		// Custom variables list
		createCustomVariablesComponent();
	}

	@Override
	protected void initializeFormQuestionTable() {
		setFormQuestionTable(new TreeObjectTableMultiSelect());
		getFormQuestionTable().setCaption(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_ELEMENTS));
		getFormQuestionTable().setSizeFull();
		getFormQuestionTable().setRootElement(UserSessionHandler.getFormController().getForm());
		getFormQuestionTable().setSelectable(true);
		getFormQuestionTable().setNullSelectionAllowed(false);
		getFormQuestionTable().setImmediate(true);
		getFormQuestionTable().setValue(UserSessionHandler.getFormController().getForm());
		getFormQuestionTable().addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setFormVariableSelectionValues();
			}
		});
		getFormQuestionTable().collapseFrom(Category.class);
	}
}
