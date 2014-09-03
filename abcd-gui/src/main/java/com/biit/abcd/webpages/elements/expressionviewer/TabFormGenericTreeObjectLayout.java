package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;

public class TabFormGenericTreeObjectLayout extends TabLayout {
	private static final long serialVersionUID = 3878203678135358339L;
	private static final String NAME_PROPERTY = "Name";
	private TreeTable variableTable;
	private ListSelect variableSelection;
	private Button addTreeObjectButton, addVariableButton;

	public TabFormGenericTreeObjectLayout() {
		createFormVariablesScope();
	}

	/**
	 * We can select more than one element, then we add expressions separated by
	 * commas. If we select a date question or variable, then we also must
	 * select the unit for the date expression.
	 */
	private void createFormVariablesScope() {
		// Create the generic tree objects table
		initializeVariableTable();
		setSpacing(true);
		variableTable.setPageLength(8);
		addComponent(variableTable);
		setExpandRatio(variableTable, 0.5f);
		addTreeObjectButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_GENERIC_ELEMENT));
		addTreeObjectButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4754466212065015629L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (variableTable.getValue() != null) {
					addExpression(new ExpressionValueGenericVariable((GenericTreeObjectType) variableTable.getValue()));
				}
			}
		});
		addComponent(addTreeObjectButton);
		setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);

		// Create the generic variables table
		initializeVariableSelection();
		addComponent(variableSelection);
		setExpandRatio(variableSelection, 0.5f);
		addVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_GENERIC_VARIABLE));
		addVariableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1203622071346720949L;

			@Override
			public void buttonClick(ClickEvent event) {
				if ((variableSelection.getValue() != null) && (variableTable.getValue() != null)) {
					addExpression(new ExpressionValueGenericCustomVariable((GenericTreeObjectType) variableTable
							.getValue(), (CustomVariable) variableSelection.getValue()));
				}
			}
		});
		addComponent(addVariableButton);
		setComponentAlignment(addVariableButton, Alignment.TOP_RIGHT);
		setFormVariableSelectionValues();
	}

	private void initializeVariableTable() {
		variableTable = new TreeTable();
		variableTable.addContainerProperty(NAME_PROPERTY, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		variableTable.setCaption(ServerTranslate
				.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_GENERIC_ELEMENTS));
		variableTable.setSizeFull();

		Object category = variableTable.addItem(new Object[] { GenericTreeObjectType.CATEGORY.getTableName() },
				GenericTreeObjectType.CATEGORY);
		variableTable.setCollapsed(category, false);

		Object questionCategory = variableTable.addItem(
				new Object[] { GenericTreeObjectType.QUESTION_CATEGORY.getTableName() },
				GenericTreeObjectType.QUESTION_CATEGORY);
		variableTable.setParent(questionCategory, category);
		variableTable.setChildrenAllowed(questionCategory, false);

		Object group = variableTable.addItem(new Object[] { GenericTreeObjectType.GROUP.getTableName() },
				GenericTreeObjectType.GROUP);
		variableTable.setParent(group, category);
		variableTable.setCollapsed(group, false);

		Object question_group = variableTable.addItem(
				new Object[] { GenericTreeObjectType.QUESTION_GROUP.getTableName() },
				GenericTreeObjectType.QUESTION_GROUP);
		variableTable.setParent(question_group, group);
		variableTable.setChildrenAllowed(question_group, false);

		variableTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6333216923592191221L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				setFormVariableSelectionValues();
			}
		});

		variableTable.setSelectable(true);
		variableTable.setNullSelectionAllowed(false);
		variableTable.setImmediate(true);
		variableTable.setValue(category);
	}

	private void setFormVariableSelectionValues() {
		if (variableSelection != null) {
			variableSelection.setValue(null);
			variableSelection.removeAllItems();
			if (variableTable.getValue() != null) {
				List<CustomVariable> customVariables = UserSessionHandler
						.getFormController()
						.getForm()
						.getCustomVariables(((GenericTreeObjectType) variableTable.getValue()).getScope());
				for (CustomVariable customvariable : customVariables) {
					variableSelection.addItem(customvariable);
					variableSelection.setItemCaption(customvariable, customvariable.getName());
				}
				if ((customVariables != null) && !customVariables.isEmpty()) {
					variableSelection.setValue(customVariables.get(0));
				}
			}
		}
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection.setCaption(ServerTranslate
				.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_GENERIC_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	public void setvalue(ExpressionValueCustomVariable expression) {
		variableTable.setValue(expression.getReference());
	}

	public void addTreeObjectButtonClickListener(ClickListener clickListener) {
		addTreeObjectButton.addClickListener(clickListener);
	}

}
