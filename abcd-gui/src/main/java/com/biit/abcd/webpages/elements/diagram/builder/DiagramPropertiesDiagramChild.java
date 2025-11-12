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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectDiagramWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class DiagramPropertiesDiagramChild extends SecuredDiagramElementProperties<DiagramChild> {
	private static final long serialVersionUID = -1462969396162576789L;
	private DiagramChild instance;
	private FormLayout childForm;
	private FieldWithSearchButton fieldWithSearchButton;

	public DiagramPropertiesDiagramChild() {
		super(DiagramChild.class);
	}

	@Override
	public void setElementForProperties(DiagramChild element) {
		instance = element;

		childForm = new FormLayout();
		childForm.setWidth(null);

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getDiagram() != null) {
			fieldWithSearchButton.setValue(instance.getDiagram(), instance.getDiagram().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1215227801957570166L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectDiagramWindow selectAnswerWindow = new SelectDiagramWindow();
				selectAnswerWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Diagram diagram = selectAnswerWindow.getSelectedDiagram();
						if (diagram != null) {

							if (diagram.equals(instance.getParent())) {
								MessageManager.showError(LanguageCodes.ERROR_SAME_DIAGRAM);
								return;
							}

							Diagram parentDiagram = UserSessionHandler.getFormController().getForm()
									.getDiagramParent(diagram);
							if (parentDiagram != null) {
								MessageManager.showError(LanguageCodes.ERROR_DIAGRAM_IS_IN_USE);
								return;
							}

							instance.setDiagram(diagram);
							fieldWithSearchButton.setValue(diagram, diagram.getName());
							selectAnswerWindow.close();
							firePropertyUpdateListener(instance);

							AbcdLogger.info(this.getClass().getName(),
									"User '" + UserSessionHandler.getUser().getEmailAddress()
											+ "' added Child diagram " + instance.getDiagram().getName()
											+ " to Diagram node with ID:" + instance.getId() + "'.");
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_DIAGRAM);
						}
					}
				});
				selectAnswerWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -7919423573659524140L;

			@Override
			public void buttonClick(ClickEvent event) {
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' removed 'Table rule' from Table node with diagram:"
						+ (instance.getDiagram() != null ? instance.getDiagram().getName() : null) + "'.");
				instance.setDiagram(null);
				firePropertyUpdateListener(instance);
			}
		});
		childForm.addComponent(fieldWithSearchButton);

		addTab(childForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_DIAGRAM_CHILD_NODE_CAPTION),
				true, 0);
	}

	@Override
	public void updateElement() {
//		firePropertyUpdateListener(instance);
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
