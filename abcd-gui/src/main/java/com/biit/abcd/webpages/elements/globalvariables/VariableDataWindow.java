package com.biit.abcd.webpages.elements.globalvariables;

import java.sql.Timestamp;
import java.util.Date;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalCode;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
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
	private AnswerFormat format;

	public VariableDataWindow(AnswerFormat format, String title) {
		super();
		setHeight(HEIGHT);
		setWidth(WIDTH);
		this.format = format;
		setContent(generateContent());
		setModal(true);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		setCaption(title);
	}

	private Component generateContent() {
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
		VariableData variableData = createVariable();
		if (valueField.getValue() == null || !isValueFieldTypeCorrect()) {
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

		try {
			variableData.setValue(valueField.getValue());
		} catch (NotValidTypeInVariableData e) {
			MessageManager.showError(e.getMessage());
		}

		variableData.setValidFrom(new Timestamp(validFrom.getValue().getTime()));

		if (validTo == null || validTo.getValue() == null) {
			// Value set to infinite (null)
			variableData.setValidTo(null);
		} else {
			variableData.setValidTo(new Timestamp(validTo.getValue().getTime()));
		}
		return variableData;
	}

	public void setValidFromEditable(boolean editable) {
		validFrom.setEnabled(editable);
	}
	
	public void setValidToEditable(boolean editable) {
		validTo.setEnabled(editable);
	}

	public void setValidFromValue(Date newDate) {
		validFrom.setValue(newDate);
	}

	public void setValue(VariableData variable) {
		if (variable instanceof VariableDataText) {
			((TextField) valueField).setValue(variable.toString());
		} else if (variable instanceof VariableDataNumber) {
			((TextField) valueField).setValue(variable.toString());
		} else if (variable instanceof VariableDataPostalCode) {
			((TextField) valueField).setValue(variable.toString());
		} else if (variable instanceof VariableDataDate) {
			((DateField) valueField).setValue(((VariableDataDate) variable).getValue());
		}
		validFrom.setValue(variable.getValidFrom());
		validTo.setValue(variable.getValidTo());
	}

	public VariableData createVariable() {
		switch (format) {
		case TEXT:
			return new VariableDataText();
		case NUMBER:
			return new VariableDataNumber();
		case POSTAL_CODE:
			return new VariableDataPostalCode();
		default:
			return new VariableDataDate();
		}
	}

	private boolean isValueFieldTypeCorrect() {
		switch (format) {
		case NUMBER:
			try {
				Double.parseDouble(valueField.getValue().toString());
			} catch (Exception e) {
				return false;
			}
			return true;
		default:
			return true;
		}
	}
}
