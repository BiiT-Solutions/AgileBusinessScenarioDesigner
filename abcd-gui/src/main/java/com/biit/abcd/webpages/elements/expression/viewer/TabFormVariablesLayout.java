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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.abcd.webpages.components.TreeObjectTableMultiSelect;
import com.biit.form.entity.TreeObject;
import com.vaadin.ui.ListSelect;

public abstract class TabFormVariablesLayout extends TabLayout {
	private static final long serialVersionUID = 3488733953726761594L;
	private TreeObjectTable formQuestionTable;
	private ListSelect variableSelection;

	protected void createCustomVariablesComponent() {
		initializeVariableSelection();
		addComponent(variableSelection);
		setExpandRatio(variableSelection, 0.5f);
		setFormVariableSelectionValues();
	}

	protected void createFormElementsComponent() {
		initializeFormQuestionTable();
		setSpacing(true);
		formQuestionTable.setPageLength(8);
		addComponent(formQuestionTable);
		setExpandRatio(formQuestionTable, 0.5f);
	}

	public TreeObjectTable getFormQuestionTable() {
		return formQuestionTable;
	}

	/**
	 * Returns the selected list of element. All elements must be of the same
	 * class.
	 *
	 * @return a list of tree objects.
	 */
	public List<TreeObject> getSelectedFormElements() {
		List<TreeObject> selected = new ArrayList<>();
		if (formQuestionTable instanceof TreeObjectTableMultiSelect) {
			if (((((Collection<?>) formQuestionTable.getValue()) == null) || ((Collection<?>) formQuestionTable
					.getValue()).isEmpty())) {
				return selected;
			}
			Class<?> firstClass = null;
			for (Object object : ((Collection<?>) formQuestionTable.getValue())) {
				// First object select the class of the accepted elements.
				if (firstClass == null) {
					firstClass = object.getClass();
				}
				// Only add elements that have the same class.
				if (object.getClass() == firstClass) {
					selected.add((TreeObject) object);
				}
			}
		} else {
			if (formQuestionTable.getValue() == null) {
				return selected;
			}
			selected.add((TreeObject) formQuestionTable.getValue());
		}
		return selected;
	}

	public List<ExpressionValueCustomVariable> getValues() {
		if (getSelectedFormElements().isEmpty() || (variableSelection.getValue() == null)) {
			return null;
		}
		List<ExpressionValueCustomVariable> variables = new ArrayList<>();
		for (TreeObject object : getSelectedFormElements()) {
			variables.add(new ExpressionValueCustomVariable(object, (CustomVariable) variableSelection.getValue()));
		}
		return variables;
	}

	public ListSelect getVariableSelection() {
		return variableSelection;
	}

	protected abstract void initializeFormQuestionTable();

	protected void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection
				.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	public void setFormQuestionTable(TreeObjectTable formQuestionTable) {
		this.formQuestionTable = formQuestionTable;
	}

	protected void setFormVariableSelectionValues() {
		if (variableSelection != null) {
			variableSelection.setValue(null);
			variableSelection.removeAllItems();
			if (!getSelectedFormElements().isEmpty()) {
				List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
						.getCustomVariables(getSelectedFormElements().get(0));
				if ((customVariables != null) && !customVariables.isEmpty()) {
					Collections.sort(customVariables);
					for (CustomVariable customvariable : customVariables) {
						variableSelection.addItem(customvariable);
						variableSelection.setItemCaption(customvariable, customvariable.getName());
					}
				}
			}
		}
	}

	public void setValue(TreeObject element) {
		formQuestionTable.setValue(element);
	}

	public void setValue(ExpressionValueCustomVariable expression) {
		setValue(expression.getReference());
		variableSelection.setValue(expression.getVariable());
	}

	public void setVariableSelection(ListSelect variableSelection) {
		this.variableSelection = variableSelection;
	}

	public void clearSelection() {
		if (formQuestionTable != null) {
			formQuestionTable.setValue(null);
		}
		if (variableSelection != null) {
			variableSelection.setValue(null);
		}
	}
}
