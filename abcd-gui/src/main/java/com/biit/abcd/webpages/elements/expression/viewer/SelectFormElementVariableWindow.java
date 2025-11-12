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

import java.util.Collections;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class SelectFormElementVariableWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;
	private VerticalLayout secondComponent;

	private FormQuestionTable formQuestionTable;
	private ListSelect variableSelection;

	public SelectFormElementVariableWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(true);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	public Component generateComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_CAPTION));

		firstComponent = new VerticalLayout();
		firstComponent.setSizeFull();
		firstComponent.setImmediate(true);
		secondComponent = new VerticalLayout();
		secondComponent.setSizeFull();
		secondComponent.setImmediate(true);

		rootLayout.addComponent(firstComponent);
		rootLayout.addComponent(secondComponent);

		initializeFormQuestionTable();
		firstComponent.addComponent(formQuestionTable);
		initializeVariableSelection();
		secondComponent.addComponent(variableSelection);

		// Initialize value of formQuestionTable.
		formQuestionTable.setValue(UserSessionHandler.getFormController().getForm());
		initializeVariableSelectionValues(UserSessionHandler.getFormController().getForm());

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_ELEMENTS));
		formQuestionTable.setSizeFull();
		formQuestionTable.setRootElement((Form) UserSessionHandler.getFormController().getForm());
		formQuestionTable.setSelectable(true);
		formQuestionTable.setNullSelectionAllowed(false);
		formQuestionTable.setImmediate(true);
		formQuestionTable.setValue(UserSessionHandler.getFormController().getForm());
		formQuestionTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TreeObject treeElement = (TreeObject) event.getProperty().getValue();
				initializeVariableSelectionValues(treeElement);
			}
		});
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	private void initializeVariableSelectionValues(TreeObject treeObject) {
		variableSelection.setValue(null);
		variableSelection.removeAllItems();
		if (treeObject != null) {
			List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm().getCustomVariables(treeObject);
			if (customVariables != null && !customVariables.isEmpty()) {
				Collections.sort(customVariables);
				for (CustomVariable customvariable : customVariables) {
					variableSelection.addItem(customvariable);
					variableSelection.setItemCaption(customvariable, customvariable.getName());
				}
				variableSelection.setValue(customVariables.get(0));
			}
		}
	}

	public ExpressionValueCustomVariable getValue() {
		if (formQuestionTable.getValue() == null || variableSelection.getValue() == null) {
			return null;
		}
		return new ExpressionValueCustomVariable((TreeObject) formQuestionTable.getValue(), (CustomVariable) variableSelection.getValue());
	}

	public void setValue(ExpressionValueCustomVariable expression) {
		formQuestionTable.setValue(expression.getReference());
		variableSelection.setValue(expression.getVariable());
	}

	/**
	 * Collapse the tree in a specific hierarchy level to inner levels. The
	 * level is specified by a class.
	 *
	 * @param collapseFrom
	 */
	public void collapseFrom(Class<?> collapseFrom) {
		formQuestionTable.collapseFrom(collapseFrom);
	}

}
