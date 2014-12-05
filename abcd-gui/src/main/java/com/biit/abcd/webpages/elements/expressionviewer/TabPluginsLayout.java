package com.biit.abcd.webpages.elements.expressionviewer;

import com.vaadin.ui.Table;

public class TabPluginsLayout extends TabLayout {
	private static final long serialVersionUID = -5476757280678114649L;
	private static final String NAME_PROPERTY = "Name";

	private Table pluginsTable;

	public TabPluginsLayout() {
		createContent();
	}

	private void createContent() {
		// Create the generic tree objects table
		createPluginsTable();
		setSpacing(true);
		getPluginsTable().setPageLength(8);
		addComponent(getPluginsTable());
		setExpandRatio(getPluginsTable(), 0.5f);
//		addTreeObjectButton = new Button(
//				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_GENERIC_ELEMENT));
//		addTreeObjectButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = -4754466212065015629L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if (getPluginsTable().getValue() != null) {
//					addExpression(new ExpressionValueString();
//				}
//			}
//		});
//		addComponent(addTreeObjectButton);
//		setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);
	}
	
	private void createPluginsTable() {
//		setPluginsTable(new Table());
//		getPluginsTable().addContainerProperty(NAME_PROPERTY, String.class, null,
//				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
//		getPluginsTable().setCaption(ServerTranslate
//				.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_GENERIC_ELEMENTS));
//		getPluginsTable().setSizeFull();
//
//		Object question_group = variableTable.addItem(
//				new Object[] { GenericTreeObjectType.QUESTION_GROUP.getTableName() },
//				GenericTreeObjectType.QUESTION_GROUP);
//		variableTable.setParent(question_group, group);
//		variableTable.setChildrenAllowed(question_group, false);
//
//		variableTable.addValueChangeListener(new ValueChangeListener() {
//			private static final long serialVersionUID = 6333216923592191221L;
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				setFormVariableSelectionValues();
//			}
//		});
//
//		getPluginsTable().setSelectable(true);
//		getPluginsTable().setNullSelectionAllowed(false);
//		getPluginsTable().setImmediate(true);
	}

	private Table getPluginsTable() {
		return pluginsTable;
	}

	private void setPluginsTable(Table pluginsTable) {
		this.pluginsTable = pluginsTable;
	}
}
