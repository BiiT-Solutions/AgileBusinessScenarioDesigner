package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectAnswerWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class JsonDiagramPropertiesLink extends PropertiesForClassComponent<DiagramLink> {
	private static final long serialVersionUID = 6308407654774598230L;
	private DiagramLink instance;
	private FormLayout linkForm;
	private TextField diagramElementLabel;
	private TextField inputFieldValue;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesLink() {
		super(DiagramLink.class);
	}

	@Override
	public void setElementAbstract(DiagramLink element) {
		instance = element;

		linkForm = new FormLayout();
		linkForm.setWidth(null);

		if (instance.getSourceElement() instanceof DiagramFork) {
			DiagramFork fork = (DiagramFork) element.getSourceElement();
			if (fork.getQuestion() != null) {
				if (fork.getQuestion().getAnswerType() == AnswerType.INPUT) {
					setInputFieldValue();
				} else {
					setSelectAnswer(fork.getQuestion());
				}
			}
		} else {
			setDiagramElementLabel();
		}

		addTab(linkForm, "TODO - diagramLinkExprProperties", true, 0);
	}

	private void setDiagramElementLabel() {
		diagramElementLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		if (instance.getText() == null) {
			diagramElementLabel.setValue("");
		} else {
			diagramElementLabel.setValue(instance.getText());
		}
		linkForm.addComponent(diagramElementLabel);
	}

	private void setInputFieldValue() {
		inputFieldValue = new TextField(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
		if (instance.getAnswerExpression() == null) {
			inputFieldValue.setValue("");
		} else {
			inputFieldValue.setValue(instance.getAnswerExpression());
		}
		linkForm.addComponent(inputFieldValue);
	}

	private void setSelectAnswer(final Question question) {
		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getAnswer() != null) {
			fieldWithSearchButton.setValue(instance.getAnswer(), instance.getAnswer().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1215227801957570166L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectAnswerWindow selectAnswerWindow = new SelectAnswerWindow(question);
				selectAnswerWindow.addAcceptAcctionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						Answer answer = selectAnswerWindow.getSelectedTableValue();
						if(answer!=null){
							instance.setAnswer(answer);
							instance.setText(answer.getName());
							fieldWithSearchButton.setValue(answer, answer.getName());
							selectAnswerWindow.close();
							firePropertyUpdateListener(instance);
						}else{
							MessageManager.showError(LanguageCodes.ERROR_SELECT_ANSWER);
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
				instance.setAnswer(null);
				instance.setText("");
				firePropertyUpdateListener(instance);
			}
		});
		linkForm.addComponent(fieldWithSearchButton);
	}

	private void updateDiagramElementLabel() {
		instance.setText(diagramElementLabel.getValue());
	}

	@Override
	public void updateElement() {
		DiagramElement element = instance.getSourceElement();
		if (element instanceof DiagramFork) {
			DiagramFork fork = (DiagramFork) element;
			if(fork.getQuestion()!=null){
				if(fork.getQuestion().getAnswerType()==AnswerType.INPUT){
					instance.setAnswerExpression(inputFieldValue.getValue());
				}
			}
		} else {
			updateDiagramElementLabel();
		}
		firePropertyUpdateListener(instance);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}