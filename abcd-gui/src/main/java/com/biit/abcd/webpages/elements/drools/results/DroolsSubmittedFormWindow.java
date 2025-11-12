package com.biit.abcd.webpages.elements.drools.results;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.SaveActionListener;
import com.biit.abcd.webpages.components.SaveAsButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.elements.form.manager.SaveDroolsRulesAction;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DroolsSubmittedFormWindow extends AcceptCancelWindow {

    private static final long serialVersionUID = -2904744459099883988L;

    private String orbeonAppName;
    private String orbeonFormName;
    private String orbeonDocumentId;

    public DroolsSubmittedFormWindow() {
        super();
        setCaption(ServerTranslate.translate(LanguageCodes.SUBMITTED_FORM_INFORMATION_CAPTION));
        setWidth("40%");
        setHeight("40%");
        setClosable(false);
        setModal(true);
        setResizable(false);
        setContent(generateContent());
    }

    @Override
    protected void generateAcceptCancelButton() {
        acceptButton = new SaveAsButton(LanguageCodes.ACCEPT_BUTTON_CAPTION, ThemeIcon.ACCEPT,
                LanguageCodes.ACCEPT_BUTTON_TOOLTIP, IconSize.SMALL, new SaveDroolsRulesAction());
        ((SaveAsButton) acceptButton).addSaveActionListener(() -> {
            fireAcceptActionListeners();
            close();
        });

        cancelButton = new IconButton(LanguageCodes.CANCEL_BUTTON_CAPTION, ThemeIcon.CANCEL,
                LanguageCodes.CANCEL_BUTTON_TOOLTIP, IconSize.SMALL, (ClickListener) event ->
        {
            fireCancelActionListeners();
            close();
        });
    }

    private Component generateContent() {
        VerticalLayout layout = new VerticalLayout();
        // Create content
        TextField appNameField = new TextField("App name: ");
        appNameField.setImmediate(true);
        appNameField.setWidth("100%");
        appNameField.setInputPrompt("WebForms");
        appNameField.setId("droolsWebFormsInput");
        appNameField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2834862614448446156L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                orbeonAppName = String.valueOf(event.getProperty().getValue());
            }
        });
        // appNameField.setValue("WebForms");

        TextField formNameField = new TextField("Form name: ");
        formNameField.setImmediate(true);
        formNameField.setWidth("100%");
        formNameField.setInputPrompt("Form Name");
        formNameField.setId("droolsFormNameInput");
        formNameField.addValueChangeListener((ValueChangeListener) event ->
                orbeonFormName = String.valueOf(event.getProperty().getValue()));
        // formNameField.setValue("De_Haagse_Passage_v2");

        TextField documentIdField = new TextField("Form ID: ");
        documentIdField.setImmediate(true);
        documentIdField.setWidth("100%");
        documentIdField.setInputPrompt("Form ID");
        documentIdField.setId("droolsFormIdInput");
        documentIdField.addValueChangeListener((ValueChangeListener) event ->
                orbeonDocumentId = String.valueOf(event.getProperty().getValue()));
        // documentIdField.setValue("370023c797b9b9b691ed0e64a559f6adb7971df5");

        layout.addComponent(appNameField);
        layout.addComponent(formNameField);
        layout.addComponent(documentIdField);
        layout.setSizeFull();
        layout.setMargin(true);
        return layout;
    }

    public String getOrbeonAppName() {
        return orbeonAppName;
    }

    public String getOrbeonFormName() {
        return orbeonFormName;
    }

    public String getOrbeonDocumentId() {
        return orbeonDocumentId;
    }

}
