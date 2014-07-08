package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectQuestionWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesFork extends PropertiesForClassComponent<DiagramFork> {
	private static final long serialVersionUID = -5767909479835775870L;
	private DiagramFork instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesFork() {
		super(DiagramFork.class);
	}

	@Override
	public void setElementAbstract(DiagramFork element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORK_QUESTION_CAPTION));
		fieldWithSearchButton.setNullCaption("Fork");
		fieldWithSearchButton.setValue(null);
		if (instance.getQuestion() != null) {
			fieldWithSearchButton.setValue(instance.getQuestion(), instance.getQuestion().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1427800999596843191L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectQuestionWindow questionWindow = new SelectQuestionWindow();
				questionWindow.addAcceptAcctionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (questionWindow.getValue() != null) {
							fieldWithSearchButton.setValue(questionWindow.getValue().getReference(), questionWindow
									.getValue().getReference().getName());
							instance.setQuestion((Question) questionWindow.getValue().getReference());
							firePropertyUpdateListener(instance);
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_TABLE);
						}
					}
				});
				questionWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -3314196233359245226L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setQuestion(null);
				firePropertyUpdateListener(instance);
			}
		});

		FormLayout forkForm = new FormLayout();
		forkForm.setWidth(null);
		forkForm.addComponent(fieldWithSearchButton);

		addTab(forkForm, "TODO - JsonDiagramProperties Fork", true, 0);
	}

	@Override
	public void updateElement() {
		// All the updates are done in the field directly.
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}