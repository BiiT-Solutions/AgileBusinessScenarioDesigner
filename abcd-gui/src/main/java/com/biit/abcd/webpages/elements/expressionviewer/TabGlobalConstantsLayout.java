package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class TabGlobalConstantsLayout extends TabLayout {
	private static final long serialVersionUID = 3488733953726761594L;

	public TabGlobalConstantsLayout() {
		createGlobalConstantsElements(UserSessionHandler.getGlobalVariablesController().getGlobalVariables());
	}

	private void createGlobalConstantsElements(List<GlobalVariable> globalVariables) {
		this.removeAllComponents();
		if (globalVariables != null) {
			for (GlobalVariable globalVariable : globalVariables) {
				addItem(globalVariable);
			}
		}
	}

	private void addItem(final GlobalVariable globalVariable) {
		Button globalVariableButton = new Button(globalVariable.getName(), new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionValueGlobalConstant exprValue = new ExpressionValueGlobalConstant(globalVariable);
				addExpression(exprValue);
			}
		});
		globalVariableButton.setWidth("100%");
		//This style hides text overflow.
		globalVariableButton.addStyleName("v-expression-button-selector");
		addComponent(globalVariableButton);
		setComponentAlignment(globalVariableButton, Alignment.MIDDLE_CENTER);
	}

}
