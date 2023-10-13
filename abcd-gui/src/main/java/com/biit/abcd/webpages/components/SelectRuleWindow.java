package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SelectRuleWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -493933078596849550L;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;

	private SelectDroolsRule formRuleTable;

	public SelectRuleWindow() {
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

		rootLayout.addComponent(firstComponent);

		initializeFormQuestionTable();
		firstComponent.addComponent(formRuleTable);

		// Initialize value of formQuestionTable.
		formRuleTable.setValue(UserSessionHandler.getFormController().getForm().getRules());

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formRuleTable = new SelectDroolsRule();
		formRuleTable.setCaption("Rules");
		formRuleTable.setSizeFull();
		formRuleTable.setSelectable(true);
		for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
			formRuleTable.addRow(rule);
		}
		formRuleTable.sort();
	}

	public Rule getValue() {
		return formRuleTable.getSelectedRule();
	}

}
