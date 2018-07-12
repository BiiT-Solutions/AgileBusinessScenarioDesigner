package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.Collections;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class SelectFormGenericVariablesWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -4212298247094386855L;
	private static final String NAME_PROPERTY = "Name";

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;
	private VerticalLayout secondComponent;

	private TreeTable variableTable;
	private ListSelect variableSelection;

	public SelectFormGenericVariablesWindow() {
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

		initializeVariableTable();
		firstComponent.addComponent(variableTable);
		initializeVariableSelection();
		secondComponent.addComponent(variableSelection);
		setFormVariableSelectionValues();

		return rootLayout;
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
				List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
						.getCustomVariables(((GenericTreeObjectType) variableTable.getValue()).getScope());
				if ((customVariables != null) && !customVariables.isEmpty()) {
					Collections.sort(customVariables);
					for (CustomVariable customvariable : customVariables) {
						variableSelection.addItem(customvariable);
						variableSelection.setItemCaption(customvariable, customvariable.getName());
					}
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

	public ExpressionValueGenericCustomVariable getValue() {
		if (variableTable.getValue() == null || variableSelection.getValue() == null) {
			return null;
		}
		return new ExpressionValueGenericCustomVariable((GenericTreeObjectType) variableTable.getValue(),
				(CustomVariable) variableSelection.getValue());
	}

	public void setvalue(ExpressionValueGenericCustomVariable expression) {
		variableTable.setValue(expression.getType());
		setFormVariableSelectionValues();
		variableSelection.setValue(expression.getVariable());
	}
}