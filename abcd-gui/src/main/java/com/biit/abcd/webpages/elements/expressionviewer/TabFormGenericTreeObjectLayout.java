package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.GenericTreeObjectScope;
import com.biit.abcd.persistence.entity.GenericTreeObjectVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericTreeObjectVariable;
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
	private Button addTreeObjectButton;

	public TabFormGenericTreeObjectLayout() {
		createFormVariablesScope();
	}

	/**
	 * We can select more than one element, then we add expressions separated by
	 * commas. If we select a date question or variable, then we also must
	 * select the unit for the date expression.
	 */
	private void createFormVariablesScope() {
		initializeVariableTable();
		setSpacing(true);
		variableTable.setPageLength(10);
		addComponent(variableTable);
		setExpandRatio(variableTable, 0.5f);
		addTreeObjectButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_GENERIC_ELEMENT));
		addTreeObjectButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4754466212065015629L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (variableTable.getValue() != null) {
					// Add element.
					final ExpressionValueGenericTreeObjectVariable functionAllVariable = new ExpressionValueGenericTreeObjectVariable();
					functionAllVariable.setVariable((GenericTreeObjectVariable) variableTable.getValue());
					addExpression(functionAllVariable);
				}
			}
		});
		addComponent(addTreeObjectButton);
		setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);
	}

	private void initializeVariableTable() {
		variableTable = new TreeTable();
		variableTable.addContainerProperty(NAME_PROPERTY, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		variableTable.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_GENERIC_ELEMENTS));
		variableTable.setSizeFull();

		Object categoryItem = variableTable.addItem(new Object[] { "Categories" },
				new GenericTreeObjectVariable(GenericTreeObjectScope.CATEGORY));

		Object questionCategoryItem = variableTable.addItem(new Object[] { "Questions" },
				new GenericTreeObjectVariable(GenericTreeObjectScope.QUESTION_CATEGORY));
		variableTable.setParent(questionCategoryItem, categoryItem);
		variableTable.setChildrenAllowed(questionCategoryItem, false);

		Object groupItem = variableTable.addItem(new Object[] { "Groups" },
				new GenericTreeObjectVariable(GenericTreeObjectScope.GROUP));
		variableTable.setParent(groupItem, categoryItem);

		Object questionGroupItem = variableTable.addItem(new Object[] { "Questions" },
				new GenericTreeObjectVariable(GenericTreeObjectScope.QUESTION_GROUP));
		variableTable.setParent(questionGroupItem, groupItem);
		variableTable.setChildrenAllowed(questionGroupItem, false);

		variableTable.setSelectable(true);
		variableTable.setNullSelectionAllowed(false);
		variableTable.setImmediate(true);
		variableTable.setValue(categoryItem);
	}

	public void setvalue(ExpressionValueCustomVariable expression) {
		variableTable.setValue(expression.getReference());
	}

	public void addTreeObjectButtonClickListener(ClickListener clickListener) {
		addTreeObjectButton.addClickListener(clickListener);
	}

}
