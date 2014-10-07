package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.elements.decisiontable.AddNewAnswerExpressionWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

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
						+ "' added expression " + diagramLink.getExpressionChain().getRepresentation()
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
			private static final long serialVersionUID = -1215227801957570166L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Generate a expression with the question not editable.
				final ExpressionValueTreeObjectReference questionExpression = (ExpressionValueTreeObjectReference) diagramLink
						.getExpressionChain().getExpressions().get(0);
				ExpressionChain answerExpressionWithQuestion = diagramLink.getExpressionChain().generateCopy();

				final AddNewAnswerExpressionWindow addNewAnswerExpressionWindow = new AddNewAnswerExpressionWindow(fork
						.getReference(), answerExpressionWithQuestion);
				addNewAnswerExpressionWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						diagramLink.getExpressionChain().setExpressions(
								addNewAnswerExpressionWindow.getExpressionChain().getExpressions());
						diagramLink.getExpressionChain().addExpression(0, questionExpression);
						diagramLink.getExpressionChain().getExpressions().get(0).setEditable(false);
						updateText(fieldWithSearchButton);
						addNewAnswerExpressionWindow.close();
					}
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
			private static final long serialVersionUID = -7919423573659524140L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Remove all but not the fork element.
				Expression auxExp = diagramLink.getExpressionChain().getExpressions().get(0);
				diagramLink.resetExpressions(auxExp);
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
		return new HashSet<AbstractComponent>(Arrays.asList(fieldWithSearchButton));
	}
}