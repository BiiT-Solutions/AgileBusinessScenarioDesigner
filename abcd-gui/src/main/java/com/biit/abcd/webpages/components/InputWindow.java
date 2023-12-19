package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class InputWindow extends AcceptCancelWindow {
    private static final long serialVersionUID = 361486551550136464L;
    private static final String WIDTH = "400px";
    private static final String HEIGHT = "250px";
    private static final String FIELD_WIDTH = "150px";

    private AbstractField<String> inputValue;

    private FormLayout formLayout;
    private final String label;

    public InputWindow(String label) {
        super();
        this.label = label;
        setContent(generateContent());
        setResizable(false);
        setDraggable(false);
        setClosable(false);
        setModal(true);
        setWidth(WIDTH);
        setHeight(HEIGHT);
    }

    public String getInputValue() {
        return inputValue.getConvertedValue().toString();
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

        rootLayout.addComponent(formLayout);
        rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

        return rootLayout;
    }

    private void createTextField() {
        if (inputValue != null) {
            formLayout.removeComponent(inputValue);
        }

        inputValue = new TextField(label);

        inputValue.setImmediate(true);
        inputValue.setWidth(FIELD_WIDTH);
        ((TextField) inputValue).setNullRepresentation("");
        inputValue.focus();

        formLayout.addComponent(inputValue);
    }

    public void setInputValue(String inputValue) {
        if (inputValue != null) {
            ((TextField) this.inputValue).setValue(inputValue);
        }
    }
}
