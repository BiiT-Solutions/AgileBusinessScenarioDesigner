package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SelectQuestionWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -493933078596849550L;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;

	private FormQuestionTable formQuestionTable;

	public SelectQuestionWindow() {
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
		firstComponent.addComponent(formQuestionTable);

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
	}

	public ExpressionValueTreeObjectReference getValue() {
		if (formQuestionTable.getValue() == null || !(formQuestionTable.getValue() instanceof Question)) {
			return null;
		}
		ExpressionValueTreeObjectReference reference = new ExpressionValueTreeObjectReference();
		reference.setReference((TreeObject) formQuestionTable.getValue());
		return reference;
	}

}
