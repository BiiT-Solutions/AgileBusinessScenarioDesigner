package com.biit.abcd.webpages.elements.diagram.builder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectRuleWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DiagramPropertiesRule extends SecuredDiagramElementProperties<DiagramRule> {
    private static final long serialVersionUID = -5724748150445403970L;
    private DiagramRule instance;
    private FieldWithSearchButton fieldWithSearchButton;

    public DiagramPropertiesRule() {
        super(DiagramRule.class);
    }

    @Override
    protected void setElementForProperties(DiagramRule element) {
        instance = element;

        fieldWithSearchButton = new FieldWithSearchButton(
                ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORK_QUESTION_CAPTION));
        fieldWithSearchButton.setNullCaption("Rule");
        fieldWithSearchButton.setValue(null);
        if (instance.getRule() != null) {
            fieldWithSearchButton.setValue(instance.getRule(), instance.getRule().getName());
        }
        fieldWithSearchButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final SelectRuleWindow ruleWindow = new SelectRuleWindow();
                ruleWindow.addAcceptActionListener(new AcceptActionListener() {

                    @Override
                    public void acceptAction(AcceptCancelWindow window) {
                        if (ruleWindow.getValue() != null) {
                            fieldWithSearchButton.setValue(ruleWindow.getValue(), ruleWindow.getValue().getName());
                            instance.setRule(ruleWindow.getValue());
                            firePropertyUpdateListener(instance);
                            AbcdLogger.info(this.getClass().getName(), "User '"
                                    + UserSessionHandler.getUser().getEmailAddress() + "' added rule "
                                    + instance.getRule().getName() + " to Rule node with ID:" + instance.getId() + "'.");
                            window.close();
                        } else {
                            MessageManager.showError(LanguageCodes.ERROR_SELECT_TABLE);
                        }
                    }
                });
                ruleWindow.showCentered();
            }
        });
        fieldWithSearchButton.addRemoveClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' removed 'Table rule' from Table node with rule:"
                        + (instance.getRule() != null ? instance.getRule().getName() : null) + "'.");
                instance.setRule(null);
                firePropertyUpdateListener(instance);
            }
        });

        FormLayout forkForm = new FormLayout();
        forkForm.setWidth(null);
        forkForm.addComponent(fieldWithSearchButton);

        addTab(forkForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_RULE_NODE_CAPTION), true, 0);
    }

    @Override
    protected void updateElement() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Set<AbstractComponent> getProtectedElements() {
        return new HashSet<AbstractComponent>(Arrays.asList(fieldWithSearchButton));
    }

}
