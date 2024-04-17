package com.biit.abcd.webpages.components;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.language.UserLocaleStringToDoubleConverter;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import java.text.ParseException;
import java.util.Date;

public class StringInputWindow extends AcceptCancelWindow {
    private static final long serialVersionUID = 361486551550136464L;
    private static final String WIDTH = "400px";
    private static final String HEIGHT = "250px";
    private static final String FIELD_WIDTH = "75%";

    private AbstractField<?> expressionValue;
    private ComboBox expressionType;

    private FormLayout formLayout;

    public StringInputWindow() {
        super();
        setContent(generateContent());
        setResizable(true);
        setDraggable(false);
        setClosable(false);
        setModal(true);
        setWidth(WIDTH);
        setHeight(HEIGHT);
    }

    public String getValue() {
        if (expressionValue != null) {
            try {
                expressionValue.validate();
                if (getFormat().equals(AnswerFormat.NUMBER) && expressionValue.getConvertedValue() != null) {
                    return expressionValue.getConvertedValue().toString();
                } else if (getFormat().equals(AnswerFormat.DATE)) {
                    return ExpressionValueTimestamp.getFormatter().format((Date) expressionValue.getValue());
                }
                return expressionValue.getValue().toString();
            } catch (InvalidValueException e) {
                // Error shown to the user in the caller class
            }
        }
        return null;
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

        expressionType = new ComboBox(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TYPE));
        expressionType.setImmediate(true);
        for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
            expressionType.addItem(answerFormatUi.getAnswerFormat());
            expressionType.setItemCaption(answerFormatUi.getAnswerFormat(), ServerTranslate.translate(answerFormatUi.getLanguageCode()));
        }
        expressionType.setValue(AnswerFormatUi.values()[0].getAnswerFormat());
        expressionType.addValueChangeListener((ValueChangeListener) event -> {
            createTextField();
            setLocale();
            setPromt();
        });

        expressionType.setNullSelectionAllowed(false);
        expressionType.setWidth(FIELD_WIDTH);

        formLayout.addComponent(expressionType);

        createTextField();

        rootLayout.addComponent(formLayout);
        rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

        setPromt();
        return rootLayout;
    }

    private void createTextField() {
        if (expressionValue != null) {
            formLayout.removeComponent(expressionValue);
        }

        if (expressionType.getValue().equals(AnswerFormat.DATE)) {
            expressionValue = new DateField(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
        } else {
            expressionValue = new TextField(ServerTranslate.translate(LanguageCodes.EXPRESSION_INPUT_WINDOW_TEXTFIELD));
        }
        expressionValue.setImmediate(true);
        expressionValue.setWidth(FIELD_WIDTH);
        if (expressionValue instanceof TextField) {
            ((TextField) expressionValue).setNullRepresentation("");
        }
        expressionValue.focus();

        formLayout.addComponent(expressionValue);
    }

    public void setValue(String value) {
        if (value != null) {
            if (getFormat().equals(AnswerFormat.NUMBER)) {
                try {
                    ObjectProperty<Double> property = new ObjectProperty<Double>(Double.parseDouble(value));
                    expressionValue.setPropertyDataSource(property);
                } catch (NumberFormatException nfe) {
                    setValue(null);
                }
            } else if (getFormat().equals(AnswerFormat.TEXT)) {
                ((TextField) expressionValue).setValue(value);
            } else if (getFormat().equals(AnswerFormat.DATE)) {
                try {
                    ((DateField) expressionValue).setValue(ExpressionValueTimestamp.getFormatter().parse(value));
                } catch (ReadOnlyException | ConversionException | ParseException e) {
                    setValue(null);
                }
            } else {
                setValue(null);
            }
        }
    }

    public void setFormat(AnswerFormat format) {
        expressionType.setValue(format);
    }

    private void setPromt() {
        switch (getFormat()) {
            case DATE:
                break;
            case NUMBER:
                ((TextField) expressionValue).setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_FLOAT));
                break;
            case POSTAL_CODE:
                ((TextField) expressionValue).setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_POSTAL_CODE));
                expressionValue.addValidator(new RegexpValidator(AbcdConfigurationReader.getInstance().getPostalCodeMask(), ServerTranslate
                        .translate(LanguageCodes.ERROR_DATA_FORMAT_INVALID)));
                break;
            case MULTI_TEXT:
            case TEXT:
                ((TextField) expressionValue).setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_TEXT));
                break;
        }
    }

    private void setLocale() {
        switch (getFormat()) {
            case DATE:
                break;
            case NUMBER:
                ((TextField) expressionValue).setConverter(new UserLocaleStringToDoubleConverter());
                break;
            case POSTAL_CODE:
            case MULTI_TEXT:
            case TEXT:
                expressionValue.setConverter(String.class);
                break;
        }
    }

    public void enableExpressionType(boolean enabled) {
        expressionType.setEnabled(enabled);
    }
}
