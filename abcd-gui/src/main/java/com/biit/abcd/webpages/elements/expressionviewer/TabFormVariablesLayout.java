package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.abcd.webpages.components.TreeObjectTableMultiSelect;
import com.biit.form.TreeObject;
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
	 * @return
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
				for (CustomVariable customvariable : customVariables) {
					variableSelection.addItem(customvariable);
					variableSelection.setItemCaption(customvariable, customvariable.getName());
				}
			}
		}
	}

	public void setValue(ExpressionValueCustomVariable expression) {
		formQuestionTable.setValue(expression.getReference());
		variableSelection.setValue(expression.getVariable());
	}

	public void setVariableSelection(ListSelect variableSelection) {
		this.variableSelection = variableSelection;
	}
}
