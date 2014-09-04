package com.biit.abcd.webpages.components;

import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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

	private TextField expressionName;
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
		return expressionName.getValue();
	}

	public AnswerFormat getFormat() {
		return (AnswerFormat) expressionType.getValue();
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

		expressionName = new TextField(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
		expressionName.setImmediate(true);
		expressionName.setWidth(FIELD_WIDTH);
		expressionType = new ComboBox(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TYPE));
		expressionType.setImmediate(true);
		expressionType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -415040440196580949L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setPromt();
			}
		});

		for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
			expressionType.addItem(answerFormatUi.getAnswerFormat());
			expressionType.setItemCaption(answerFormatUi.getAnswerFormat(),
					ServerTranslate.translate(answerFormatUi.getLanguageCode()));
		}
		expressionType.setNullSelectionAllowed(false);
		expressionType.setValue(AnswerFormatUi.values()[0].getAnswerFormat());

		expressionType.setWidth(FIELD_WIDTH);

		formLayout.addComponent(expressionName);
		formLayout.addComponent(expressionType);
		// expressionName.focus();

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		setPromt();
		return rootLayout;
	}

	public void setValue(String value) {
		expressionName.setValue(value);
	}

	public void setFormat(AnswerFormat format) {
		expressionType.setValue(format);
	}

	private void setPromt() {
		switch (getFormat()) {
		case DATE:
			expressionName.setInputPrompt("dd/mm/yy");
			break;
		case NUMBER:
			expressionName.setInputPrompt(new Float(1.234).toString());
			break;
		case POSTAL_CODE:
			expressionName.setInputPrompt("0000AA");
			break;
		case TEXT:
			expressionName.setInputPrompt("TEXT");
			break;
		}
	}
	
	public void enableExpressionType(boolean enabled){
		expressionType.setEnabled(enabled);
	}
}
