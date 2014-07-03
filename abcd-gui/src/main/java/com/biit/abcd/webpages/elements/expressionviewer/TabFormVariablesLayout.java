package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueFormCustomVariable;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;

public class TabFormVariablesLayout extends TabLayout {
	private static final long serialVersionUID = 3488733953726761594L;
	private FormQuestionTable formQuestionTable;
	private ListSelect variableSelection;
	private Button addTreeObjectButton, addVariableButton;

	public TabFormVariablesLayout() {
		createFormVariablesElements();
	}

	private void createFormVariablesElements() {
		initializeFormQuestionTable();
		this.setSpacing(true);
		formQuestionTable.setPageLength(10);
		addComponent(formQuestionTable);
		setExpandRatio(formQuestionTable, 0.5f);
		addTreeObjectButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_ELEMENT));
		addComponent(addTreeObjectButton);
		setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);
		initializeVariableSelection();
		addComponent(variableSelection);
		setExpandRatio(variableSelection, 0.5f);
		addVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_VARIABLE));
		addComponent(addVariableButton);
		setComponentAlignment(addVariableButton, Alignment.TOP_RIGHT);

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
				setFormVariableSelectionValues();
			}
		});
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection
				.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	private void setFormVariableSelectionValues() {
		variableSelection.setValue(null);
		variableSelection.removeAllItems();
		if (((TreeObject) formQuestionTable.getValue()) != null) {
			List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
					.getCustomVariables((TreeObject) formQuestionTable.getValue());
			for (CustomVariable customvariable : customVariables) {
				variableSelection.addItem(customvariable);
				variableSelection.setItemCaption(customvariable, customvariable.getName());
			}
			if (customVariables != null && !customVariables.isEmpty()) {
				variableSelection.setValue(customVariables.get(0));
			}
		}
	}

	public ExpressionValueFormCustomVariable getValue() {
		if (formQuestionTable.getValue() == null || variableSelection.getValue() == null) {
			return null;
		}
		return new ExpressionValueFormCustomVariable((TreeObject) formQuestionTable.getValue(),
				(CustomVariable) variableSelection.getValue());
	}

	public void setvalue(ExpressionValueFormCustomVariable expression) {
		formQuestionTable.setValue(expression.getQuestion());
		variableSelection.setValue(expression.getVariable());
	}

	public void addTreeObjectButtonClickListener(ClickListener clickListener) {
		addTreeObjectButton.addClickListener(clickListener);
	}

	public void addVariableButtonClickListener(ClickListener clickListener) {
		addVariableButton.addClickListener(clickListener);
	}

}
