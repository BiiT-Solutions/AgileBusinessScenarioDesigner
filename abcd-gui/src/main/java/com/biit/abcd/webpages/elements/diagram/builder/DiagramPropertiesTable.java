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

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectTableWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DiagramPropertiesTable extends SecuredDiagramElementProperties<DiagramTable> {
    private static final long serialVersionUID = -4698686607497943878L;
    private DiagramTable instance;
    private FieldWithSearchButton fieldWithSearchButton;

    public DiagramPropertiesTable() {
        super(DiagramTable.class);
    }

    @Override
    protected void setElementForProperties(DiagramTable element) {
        instance = element;

        fieldWithSearchButton = new FieldWithSearchButton("Table");
        fieldWithSearchButton.setNullCaption("Table");
        fieldWithSearchButton.setValue(null);
        if (instance.getTable() != null) {
            fieldWithSearchButton.setValue(instance.getTable(), instance.getTable().getName());
        }
        fieldWithSearchButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final SelectTableWindow tableWindow = new SelectTableWindow();
                tableWindow.addAcceptActionListener(new AcceptActionListener() {

                    @Override
                    public void acceptAction(AcceptCancelWindow window) {
                        if (tableWindow.getSelectedTableRule() != null) {
                            fieldWithSearchButton.setValue(tableWindow.getSelectedTableRule(), tableWindow
                                    .getSelectedTableRule().getName());
                            instance.setTable(tableWindow.getSelectedTableRule());
                            firePropertyUpdateListener(instance);
                            AbcdLogger.info(this.getClass().getName(), "User '"
                                    + UserSessionHandler.getUser().getEmailAddress() + "' added 'Table rule' "
                                    + instance.getTable().getName() + " to Table node with ID:" + instance.getId()
                                    + "'.");
                            window.close();
                        } else {
                            MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
                        }
                    }
                });
                tableWindow.showCentered();
            }
        });
        fieldWithSearchButton.addRemoveClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
                        + "' removed 'Table rule' from Table node with table:"
                        + (instance.getTable() != null ? instance.getTable().getName() : null) + "'.");
                instance.setTable(null);
                firePropertyUpdateListener(instance);
            }
        });

        FormLayout categoryForm = new FormLayout();
        categoryForm.setWidth(null);
        categoryForm.addComponent(fieldWithSearchButton);

        addTab(categoryForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_TABLE_NODE_CAPTION), true,
                0);
    }

    @Override
    protected void updateElement() {
        // All the updates are done in the field action directly.
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
