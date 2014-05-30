package com.biit.abcd.webpages.components;

import java.sql.Timestamp;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class VariableDataWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -2674340247381330979L;
	private static final String WIDTH = "400px";
	private static final String HEIGHT = "400px";
	private static final String FIELD_WIDTH = "400px";

	private AbstractField<?> valueField;
	private DateField validFrom;
	private DateField validTo;

	public VariableDataWindow(AnswerFormat format) {
		super();
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setContent(generateContent(format));
		setModal(true);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
	}

	private Component generateContent(AnswerFormat format) {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		FormLayout formLayout = new FormLayout();
		formLayout.setSizeUndefined();
		formLayout.setSpacing(true);

		switch (format) {
		case DATE:
			valueField = new DateField("Dat!");
			break;
		case NUMBER:
			valueField = new TextField("Num!");
		case TEXT:
			valueField = new TextField("text!");
		}
		validFrom = new DateField("na?");
		validTo = new DateField("na!");

		valueField.setWidth(FIELD_WIDTH);
		validFrom.setWidth(FIELD_WIDTH);
		validTo.setWidth(FIELD_WIDTH);

		formLayout.addComponent(valueField);
		formLayout.addComponent(validFrom);
		formLayout.addComponent(validTo);

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public VariableData getValue() {
		VariableData variableData = new VariableData();
		if (valueField.getValue() != null) {
			variableData.setValue(valueField.getValue().toString());
			variableData.setValidFrom(new Timestamp(validFrom.getValue().getTime()));
			variableData.setValidTo(new Timestamp(validTo.getValue().getTime()));
		}
		return variableData;
	}
}
