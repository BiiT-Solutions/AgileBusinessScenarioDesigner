package com.biit.abcd.webpages.elements.diagram.builder;

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

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow.ClearElementsActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.elements.decisiontable.AddNewConditionExpressionWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DiagramPropertiesLink extends SecuredDiagramElementProperties<DiagramLink> {
    private static final long serialVersionUID = 6308407654774598230L;
    private DiagramLink diagramLink;
    private FieldWithSearchButton fieldWithSearchButton;

    public DiagramPropertiesLink() {
        super(DiagramLink.class);
    }

    @Override
    public void setElementForProperties(DiagramLink diagramLink) {
        this.diagramLink = diagramLink;

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth(null);

        // Comes from a fork.
        if (diagramLink.getSourceElement() instanceof DiagramFork) {
            DiagramFork fork = (DiagramFork) diagramLink.getSourceElement();
            addTab(formLayout, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_CAPTION), true, 0);
            if (fork.getReference() != null) {
                fieldWithSearchButton = createFieldWithSearchButton(fork);
                formLayout.addComponent(fieldWithSearchButton);
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' added expression " + diagramLink.getExpressionChain().getRepresentation(true)
                        + " to Link with ID:" + diagramLink.getId() + "'.");
            }
        }
    }

    private FieldWithSearchButton createFieldWithSearchButton(final DiagramFork fork) {
        final FieldWithSearchButton fieldWithSearchButton = new FieldWithSearchButton(
                ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
        fieldWithSearchButton.setNullCaption(ServerTranslate
                .translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_NULL_CAPTION));
        fieldWithSearchButton.setValue(null);
        updateText(fieldWithSearchButton);
        fieldWithSearchButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                // Generate an expression with the question not editable.
                final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) diagramLink
                        .getExpressionChain().getExpressions().get(0);
                ExpressionChain answerExpressionWithQuestion = (ExpressionChain) diagramLink.getExpressionChain()
                        .generateCopy();

                final AddNewConditionExpressionWindow addNewAnswerExpressionWindow = new AddNewConditionExpressionWindow(fork
                        .getReference(), answerExpressionWithQuestion);
                addNewAnswerExpressionWindow.addAcceptActionListener(window -> {
                    diagramLink.getExpressionChain().setExpressions(
                            addNewAnswerExpressionWindow.getExpressionChain().getExpressions());
                    diagramLink.getExpressionChain().addExpression(0, questionExpression);
                    diagramLink.getExpressionChain().getExpressions().get(0).setEditable(false);
                    updateText(fieldWithSearchButton);
                    addNewAnswerExpressionWindow.close();
                });
                addNewAnswerExpressionWindow.addClearActionListener(new ClearElementsActionListener() {

                    @Override
                    public void clearAction(AcceptCancelClearWindow window) {
                        addNewAnswerExpressionWindow.clearSelection();
                        AbcdLogger.info(this.getClass().getName(), "User '"
                                + UserSessionHandler.getUser().getEmailAddress()
                                + "' removed expression from Link with ID:" + diagramLink.getId() + "'.");
                    }
                });
                addNewAnswerExpressionWindow.showCentered();
            }
        });
        fieldWithSearchButton.addRemoveClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                // Remove all but not the fork element.
                Expression auxExp = diagramLink.getExpressionChain().getExpressions().get(0);
                diagramLink.replaceExpressions(auxExp);
                updateText(fieldWithSearchButton);
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' removed expression from Link with ID:" + diagramLink.getId() + "'.");
            }
        });
        return fieldWithSearchButton;
    }

    public void updateText(FieldWithSearchButton fieldWithSearchButton) {
        if ((diagramLink.getExpressionChain() != null) && !diagramLink.getExpressionChain().getExpressions().isEmpty()) {
            fieldWithSearchButton.setValue(diagramLink.getExpressionChain(),
                    diagramLink.getTextWithoutFirstExpression());
        } else {
            fieldWithSearchButton.setValue(null);
        }
        firePropertyUpdateListener(diagramLink);
    }

    @Override
    public void updateElement() {
        // No update is needed all update actions are done on the component.
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        firePropertyUpdateListener(diagramLink);
    }

    @Override
    protected Set<AbstractComponent> getProtectedElements() {
        return new HashSet<>(Collections.singletonList(fieldWithSearchButton));
    }
}
