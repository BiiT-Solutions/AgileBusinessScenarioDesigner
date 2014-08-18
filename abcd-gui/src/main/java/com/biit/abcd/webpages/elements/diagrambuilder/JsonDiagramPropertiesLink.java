package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.elements.decisiontable.AddNewAnswerExpressionWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesLink extends PropertiesForClassComponent<DiagramLink> {
	private static final long serialVersionUID = 6308407654774598230L;
	private DiagramLink instance;
	private FormLayout linkForm;
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
			if (fork.getReference() != null) {
				setSelectAnswerExpression(fork);
			}

			addTab(linkForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_CAPTION), true, 0);
		}

	}

	private void setSelectAnswerExpression(final DiagramFork fork) {
		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_LINK_INPUT_FIELD_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		updateText();
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1215227801957570166L;

			@Override
			public void buttonClick(ClickEvent event) {
				final AddNewAnswerExpressionWindow addNewAnswerExpressionWindow = new AddNewAnswerExpressionWindow(fork
						.getReference(), instance.getExpressionChain());
				addNewAnswerExpressionWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						ExpressionChain expressionChain = addNewAnswerExpressionWindow.getExpressionChain();
						instance.getExpressionChain().setExpressions(expressionChain.getExpressions());
						updateText();
						addNewAnswerExpressionWindow.close();
					}
				});
				addNewAnswerExpressionWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -7919423573659524140L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.getExpressionChain().removeAllExpressions();
				updateText();
			}
		});
		linkForm.addComponent(fieldWithSearchButton);
	}

	public void updateText(){
		if ((instance.getExpressionChain() != null) && !instance.getExpressionChain().getExpressions().isEmpty()) {
			fieldWithSearchButton.setValue(instance.getExpressionChain(), instance.getExpressionChain()
					.getRepresentation());
		}else{
			fieldWithSearchButton.setValue(null);
		}
		firePropertyUpdateListener(instance);
	}

	@Override
	public void updateElement() {
		// No update is needed all update actions are done on the component.
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}