package com.biit.abcd.webpages.elements.diagram.builder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectExpressionWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DiagramPropertiesExpressions extends SecuredDiagramElementProperties<DiagramExpression> {
    private static final long serialVersionUID = 5356130114169313201L;
    private DiagramExpression instance;
    private FieldWithSearchButton fieldWithSearchButton;

    public DiagramPropertiesExpressions() {
        super(DiagramExpression.class);
    }

    @Override
    public void setElementForProperties(DiagramExpression element) {
        instance = element;

        fieldWithSearchButton = new FieldWithSearchButton(
                ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_CALCULATION_EXPRESSION_CAPTION));
        fieldWithSearchButton.setNullCaption(ServerTranslate
                .translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_CALCULATION_EXPRESSION_NULL_CAPTION));
        fieldWithSearchButton.setValue(null);
        if (instance.getExpression() != null) {
            fieldWithSearchButton.setValue(instance.getExpression(), instance.getExpression().getName());
        }
        fieldWithSearchButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final SelectExpressionWindow formExpressionWindow = new SelectExpressionWindow();
                formExpressionWindow.addAcceptActionListener(new AcceptActionListener() {

                    @Override
                    public void acceptAction(AcceptCancelWindow window) {
                        if (formExpressionWindow.getSelectedExpression() != null) {
                            fieldWithSearchButton.setValue(formExpressionWindow.getSelectedExpression(),
                                    formExpressionWindow.getSelectedExpression().getName());
                            instance.setExpression(formExpressionWindow.getSelectedExpression());
                            firePropertyUpdateListener(instance);
                            AbcdLogger.info(this.getClass().getName(), "User '"
                                    + UserSessionHandler.getUser().getEmailAddress() + "' added Expression "
                                    + instance.getExpression().getName() + " to Calculation node with ID:"
                                    + instance.getId() + "'.");
                            window.close();
                        } else {
                            MessageManager.showError(LanguageCodes.ERROR_SELECT_EXPRESSION);
                        }
                    }
                });
                formExpressionWindow.showCentered();
            }
        });
        fieldWithSearchButton.addRemoveClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' removed 'Table rule' from Table node with expression:"
                        + (instance.getExpression() != null ? instance.getExpression().getName() : null) + "'.");
                instance.setExpression(null);
                firePropertyUpdateListener(instance);
            }
        });

        FormLayout categoryForm = new FormLayout();
        categoryForm.setWidth(null);
        categoryForm.addComponent(fieldWithSearchButton);

        addTab(categoryForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORM_NODE_CAPTION), true,
                0);
    }

    @Override
    public void updateElement() {
        // All the updates are done in the field directly.
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        firePropertyUpdateListener(instance);
    }

    @Override
    protected Set<AbstractComponent> getProtectedElements() {
        return new HashSet<AbstractComponent>(Arrays.asList(fieldWithSearchButton));
    }

}