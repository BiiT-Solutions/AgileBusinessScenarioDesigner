package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
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
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' added expression " + instance.getExpressionChain().getRepresentation()
						+ " to Link with ID:" + instance.getId() + "'.");
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
						// Add the question element if it's not an input
						// question
						{
							Expression auxExp = instance.getExpressionChain().getExpressions().get(0);
							if ((auxExp instanceof ExpressionValueTreeObjectReference)
									&& !(auxExp instanceof ExpressionValueCustomVariable)
									&& (((ExpressionValueTreeObjectReference) auxExp).getReference() instanceof Question)
									&& !((Question) ((ExpressionValueTreeObjectReference) auxExp).getReference())
											.getAnswerType().equals(AnswerType.INPUT)) {
								expressionChain.getExpressions().add(0,
										instance.getExpressionChain().getExpressions().get(0));
							}
						}
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
				Expression auxExp = instance.getExpressionChain().getExpressions().get(0);
				instance.getExpressionChain().removeAllExpressions();
				instance.addExpressionToExpressionChain(auxExp);
				updateText();
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' removed expression from Link with ID:" + instance.getId() + "'.");
			}
		});
		linkForm.addComponent(fieldWithSearchButton);
	}

	public void updateText() {
		if ((instance.getExpressionChain() != null) && !instance.getExpressionChain().getExpressions().isEmpty()) {
			fieldWithSearchButton.setValue(instance.getExpressionChain(), instance.getTextWithoutFirstExpression());
		} else {
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