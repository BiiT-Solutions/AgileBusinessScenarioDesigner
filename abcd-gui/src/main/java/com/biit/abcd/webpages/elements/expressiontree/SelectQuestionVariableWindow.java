package com.biit.abcd.webpages.elements.expressiontree;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.ExprValueFormReference;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class SelectQuestionVariableWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;
	private VerticalLayout secondComponent;

	private FormQuestionTable formQuestionTable;
	private ListSelect variableSelection;

	public SelectQuestionVariableWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
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

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setCaption("TODO - Form element");
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
		variableSelection.setCaption("TODO - Variable");
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	private void initializeVariableSelectionValues(TreeObject treeObject) {
		variableSelection.setValue(null);
		variableSelection.removeAllItems();
		if (treeObject != null) {
			List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
					.getCustomVariables(treeObject);
			for (CustomVariable customvariable : customVariables) {
				variableSelection.addItem(customvariable);
				variableSelection.setItemCaption(customvariable, customvariable.getName());
			}
			if (customVariables != null && !customVariables.isEmpty()) {
				variableSelection.setValue(customVariables.get(0));
			}
		}
	}

	public ExprValueFormReference getValue() {
		if (formQuestionTable.getValue() == null || variableSelection.getValue()==null) {
			return null;
		}
		return new ExprValueFormReference((TreeObject) formQuestionTable.getValue(),
				(CustomVariable) variableSelection.getValue());
	}

}
