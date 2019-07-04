package com.biit.abcd.webpages.elements.diagram.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectExpressionWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class DiagramPropertiesSink extends SecuredDiagramElementProperties<DiagramSink> {
	private static final long serialVersionUID = -5894964889869328279L;
	private DiagramSink instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public DiagramPropertiesSink() {
		super(DiagramSink.class);
	}

	@Override
	public void setElementForProperties(DiagramSink element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SINK_EXPRESSION_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SINK_EXPRESSION_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getExpression() != null) {
			fieldWithSearchButton.setValue(instance.getExpression(), instance.getExpression().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 8612691233380693806L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectExpressionWindow formExpressionWindow = new SelectExpressionWindow();
				formExpressionWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (formExpressionWindow.getSelectedExpression() != null) {
							fieldWithSearchButton.setValue(formExpressionWindow.getSelectedExpression(),
									formExpressionWindow.getSelectedExpression().getName());
							instance.setExpression(formExpressionWindow.getSelectedExpression());
							firePropertyUpdateListener(instance);
							AbcdLogger.info(this.getClass().getName(), "User '"
									+ UserSessionHandler.getUser().getEmailAddress() + "' added expression "
									+ instance.getExpression().getRepresentation(true) + " to Sink node with ID:"
									+ instance.getId() + "'.");
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_EXPRESSION);
						}
					}
				});
				if (instance != null && instance.getExpression() != null) {
					formExpressionWindow.setSelectedExpression(instance.getExpression());
				}
				formExpressionWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -222450247941571934L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setExpression(null);
				firePropertyUpdateListener(instance);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' removed expression from Sink node with ID:" + instance.getId() + "'.");
			}
		});

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(fieldWithSearchButton);

		addTab(categoryForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SINK_NODE_CAPTION), true,
				0);
	}

	@Override
	public void updateElement() {
		// All the updates are done in the field directly.
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