package com.biit.abcd.webpages.components;

import java.sql.Timestamp;
import java.util.Date;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
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
	private static final String HEIGHT = "250px";
	private static final String FIELD_WIDTH = "150px";

	private AbstractField<?> valueField;
	private DateField validFrom;
	private DateField validTo;
	
	public VariableDataWindow(AnswerFormat format, String title) {
		super();
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setContent(generateContent(format));
		setModal(true);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		setCaption(title);
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
			valueField = new DateField();
			break;
		default:
			valueField = new TextField();
			break;
		}
		valueField.setCaption(ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE));
		validFrom = new DateField(ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALID_FROM));
		validTo = new DateField(ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALID_TO));

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
		// TODO add not valid condition.
		if (valueField.getValue() == null /* || not valid */) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.WARNING_VARIABLE_DATA_VALUE_MISSING);
			return null;
		}
		if (validFrom.getValue() == null) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
					LanguageCodes.WARNING_VARIABLE_DATA_VALID_FROM_MISSING);
			return null;
		}
		if (validTo != null && validTo.getValue() != null && validFrom.getValue().after(validTo.getValue())) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
					LanguageCodes.WARNING_VARIABLE_DATA_VALID_RANGE_WRONG);
			return null;
		}

		variableData.setValue(valueField.getValue().toString());
		variableData.setValidFrom(new Timestamp(validFrom.getValue().getTime()));
		
		if (validTo == null || validTo.getValue() == null){
			// Value set to infinite (null)
			variableData.setValidTo(null);
		}else{
			variableData.setValidTo(new Timestamp(validTo.getValue().getTime()));
		}
		return variableData;
	}

	public void setValidFromEditable(boolean editable) {
		validFrom.setEnabled(editable);
	}
	
	public void setValidFromValue(Date newDate) {
		validFrom.setValue(newDate);
	}
}
