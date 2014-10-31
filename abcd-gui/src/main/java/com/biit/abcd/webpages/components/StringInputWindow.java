package com.biit.abcd.webpages.components;

import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.language.UserLocaleStringToDoubleConverter;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class StringInputWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 361486551550136464L;
	private static final String WIDTH = "400px";
	private static final String HEIGHT = "250px";
	private static final String FIELD_WIDTH = "150px";
	private AnswerFormat previousValueType = null;

	private TextField expressionValue;
	private ComboBox expressionType;

	private FormLayout formLayout;

	public StringInputWindow() {
		super();
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(WIDTH);
		setHeight(HEIGHT);
	}

	public String getValue() {
		if (getFormat().equals(AnswerFormat.NUMBER)) {
			return expressionValue.getConvertedValue().toString();
		}
		return expressionValue.getValue();
	}

	public AnswerFormat getFormat() {
		return (AnswerFormat) expressionType.getValue();
	}

	/**
	 * Remove the expression value if the new type is not suitable to the old value.
	 */
	private void clearExpressionValue() {
		if (previousValueType != null && previousValueType != (AnswerFormat) expressionType.getValue()) {
			expressionValue.setValue(null);
		}
		previousValueType = (AnswerFormat) expressionType.getValue();
	}

	private Component generateContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setImmediate(true);

		formLayout = new FormLayout();
		formLayout.setSizeUndefined();
		formLayout.setSpacing(true);
		formLayout.setImmediate(true);

		createTextField();

		expressionType = new ComboBox(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TYPE));
		expressionType.setImmediate(true);
		for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
			expressionType.addItem(answerFormatUi.getAnswerFormat());
			expressionType.setItemCaption(answerFormatUi.getAnswerFormat(),
					ServerTranslate.translate(answerFormatUi.getLanguageCode()));
		}
		expressionType.setValue(AnswerFormatUi.values()[0].getAnswerFormat());
		expressionType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -415040440196580949L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				createTextField();
				setLocale();
				setPromt();
			}
		});

		expressionType.setNullSelectionAllowed(false);
		expressionType.setWidth(FIELD_WIDTH);

		formLayout.addComponent(expressionType);

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		setPromt();
		return rootLayout;
	}

	private void createTextField() {
		if (expressionValue != null) {
			formLayout.removeComponent(expressionValue);
		}

		expressionValue = new TextField(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
		expressionValue.setImmediate(true);
		expressionValue.setWidth(FIELD_WIDTH);
		expressionValue.setNullRepresentation("");

		formLayout.addComponent(expressionValue, 0);
	}

	public void setValue(Object value) {
		if (value instanceof Double) {
			ObjectProperty<Double> property = new ObjectProperty<Double>((Double) value);
			expressionValue.setPropertyDataSource(property);
			// } else if (value instanceof Timestamp) {
			// ObjectProperty<Date> property = new ObjectProperty<Date>(new Date(((Timestamp) value).getTime()));
			// expressionValue.setPropertyDataSource(property);
		} else {
			expressionValue.setValue(value.toString());
		}
	}

	public void setFormat(AnswerFormat format) {
		expressionType.setValue(format);
	}

	private void setPromt() {
		switch (getFormat()) {
		case DATE:
			expressionValue.setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_DATE));
			break;
		case NUMBER:
			expressionValue.setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_FLOAT));
			break;
		case POSTAL_CODE:
			expressionValue.setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_POSTAL_CODE));
			break;
		case TEXT:
			expressionValue.setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_TEXT));
			break;
		}
	}

	private void setLocale() {
		switch (getFormat()) {
		case DATE:
			// expressionValue.setConverter(new UserLocaleStringToDateConverter());
			expressionValue.setConverter(String.class);
			break;
		case NUMBER:
			expressionValue.setConverter(new UserLocaleStringToDoubleConverter());
			break;
		case POSTAL_CODE:
			expressionValue.setConverter(String.class);
			break;
		case TEXT:
			expressionValue.setConverter(String.class);
			break;
		}
	}

	public void enableExpressionType(boolean enabled) {
		expressionType.setEnabled(enabled);
	}
}
