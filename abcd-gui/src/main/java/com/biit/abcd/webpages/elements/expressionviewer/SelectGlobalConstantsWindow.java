package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class SelectGlobalConstantsWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout selectionComponent;

	private ListSelect constantSelection;

	public SelectGlobalConstantsWindow() {
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
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_GLOBAL_CONSTANT_WINDOW_CAPTION));

		selectionComponent = new VerticalLayout();
		selectionComponent.setSizeFull();
		selectionComponent.setImmediate(true);

		rootLayout.addComponent(selectionComponent);

		initializeVariableSelection();
		selectionComponent.addComponent(constantSelection);

		// Initialize value of formQuestionTable.
		initializeVariableSelectionValues(UserSessionHandler.getGlobalVariablesController().getGlobalVariables());

		return rootLayout;
	}

	private void initializeVariableSelection() {
		constantSelection = new ListSelect();
		constantSelection.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_GLOBAL_CONSTANT_WINDOW_LIST));
		constantSelection.setSizeFull();
		constantSelection.setNullSelectionAllowed(false);
		constantSelection.setImmediate(true);
	}

	private void initializeVariableSelectionValues(List<GlobalVariable> globalVariables) {
		constantSelection.setValue(null);
		constantSelection.removeAllItems();
		if (globalVariables != null) {
			for (GlobalVariable globalVariable : globalVariables) {
				constantSelection.addItem(globalVariable);
				constantSelection.setItemCaption(globalVariable, globalVariable.getName());
			}
			if (!globalVariables.isEmpty()) {
				constantSelection.setValue(globalVariables.get(0));
			}
		}
	}

	public GlobalVariable getValue() {
		if (constantSelection.getValue() == null) {
			return null;
		}
		return (GlobalVariable) constantSelection.getValue();
	}

	public void setValue(GlobalVariable globalVariable) {
		constantSelection.setValue(globalVariable);
	}

}
