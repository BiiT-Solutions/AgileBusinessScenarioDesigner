package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.elements.expressiontree.SelectQuestionWindow;
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
		System.out.println("DiagramPropertiesFork setElement: "+element+" "+element.getQuestion());
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton("Question");
		fieldWithSearchButton.setNullCaption("Fork");
		fieldWithSearchButton.setValue(null);
		System.out.println("Value of current instance: " + instance.getQuestion());
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
							fieldWithSearchButton.setValue(questionWindow.getValue().getQuestion(), questionWindow
									.getValue().getQuestion().getName());
							instance.setQuestion((Question) questionWindow.getValue().getQuestion());
							instance.getBiitText().setText(instance.getQuestion().getName());
							System.out.println("Question set: "+instance.getQuestion());
							firePropertyUpdateListener(instance);
							System.out.println("Question setB: "+instance.getQuestion());
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_QUESTION);
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
				instance.getBiitText().setText("Fork");
				firePropertyUpdateListener(instance);
			}
		});

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(fieldWithSearchButton);

		addTab(categoryForm, "TODO - JsonDiagramProperties Fork", true, 0);
	}

	@Override
	public void updateElement() {
		//All the updates are done in the field directly.
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		System.out.println("FirePropertyUpdateOnExitListener");
		firePropertyUpdateListener(instance);
	}

}