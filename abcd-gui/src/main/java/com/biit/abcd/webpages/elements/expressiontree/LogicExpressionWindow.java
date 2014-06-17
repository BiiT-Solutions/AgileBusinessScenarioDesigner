package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprWoChildLogic;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class LogicExpressionWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;
	private VerticalLayout secondComponent;
	private VerticalLayout thirdComponent;

	private FormQuestionTable formQuestionTable;
	private ListSelect variableSelection;

	public LogicExpressionWindow(ExprWoChildLogic element) {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setDraggable(false);
		setModal(true);

		setContent(generateComponent());

	}

	public Component generateComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		firstComponent = new VerticalLayout();
		firstComponent.setSizeFull();
		secondComponent = new VerticalLayout();
		secondComponent.setSizeFull();
		thirdComponent = new VerticalLayout();
		thirdComponent.setSizeFull();

		rootLayout.addComponent(firstComponent);
		rootLayout.addComponent(secondComponent);
		rootLayout.addComponent(thirdComponent);

		initializeFormQuestionTable();
		firstComponent.addComponent(formQuestionTable);
		initializeVariableSelection();
		secondComponent.addComponent(variableSelection);

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setCaption("Form element");
		formQuestionTable.setSizeFull();
		formQuestionTable.setRootElement((Form) UserSessionHandler.getFormController().getForm());
		formQuestionTable.setSelectable(true);
		formQuestionTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				TreeObject treeElement = (TreeObject)event.getProperty().getValue();
				initializeVariableSelectionValues(treeElement);
			}
		});
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection.setCaption("Variable");
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(true);
		variableSelection.addItem("nullItem");
		variableSelection.setNullSelectionItemId("nullItem");
		variableSelection.setItemCaption("nullItem", "None");
	}
	
	private void initializeVariableSelectionValues(TreeObject treeObject){
		
	}

}
