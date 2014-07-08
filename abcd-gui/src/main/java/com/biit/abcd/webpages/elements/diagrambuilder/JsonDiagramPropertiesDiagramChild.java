package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectDiagramWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesDiagramChild extends PropertiesForClassComponent<DiagramChild> {
	private static final long serialVersionUID = -1462969396162576789L;
	private DiagramChild instance;
	private FormLayout childForm;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesDiagramChild() {
		super(DiagramChild.class);
	}

	@Override
	public void setElementAbstract(DiagramChild element) {
		instance = element;

		childForm = new FormLayout();
		childForm.setWidth(null);

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getChildDiagram() != null) {
			fieldWithSearchButton.setValue(instance.getChildDiagram(), instance.getChildDiagram().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1215227801957570166L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectDiagramWindow selectAnswerWindow = new SelectDiagramWindow();
				selectAnswerWindow.addAcceptAcctionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Diagram diagram = selectAnswerWindow.getSelectedDiagram();
						if (diagram != null) {
							instance.setChildDiagram(diagram);
							fieldWithSearchButton.setValue(diagram, diagram.getName());
							selectAnswerWindow.close();
							firePropertyUpdateListener(instance);
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
				instance.setChildDiagram(null);
				firePropertyUpdateListener(instance);
			}
		});
		childForm.addComponent(fieldWithSearchButton);

		addTab(childForm, "TODO - diagramChildProperties", true, 0);
	}

	@Override
	public void updateElement() {

		firePropertyUpdateListener(instance);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}