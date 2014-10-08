package com.biit.abcd.webpages.elements.diagrambuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.SelectExpressionWindow;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class DiagramPropertiesCalculation extends SecuredDiagramElementProperties<DiagramExpression> {
	private static final long serialVersionUID = 5356130114169313201L;
	private DiagramExpression instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public DiagramPropertiesCalculation() {
		super(DiagramExpression.class);
	}

	@Override
	public void setElementForProperties(DiagramExpression element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_CALCULATION_EXPRESSION_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_CALCULATION_EXPRESSION_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getFormExpression() != null) {
			fieldWithSearchButton.setValue(instance.getFormExpression(), instance.getFormExpression().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8500340609293771339L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectExpressionWindow formExpressionWindow = new SelectExpressionWindow();
				formExpressionWindow.addAcceptActionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (formExpressionWindow.getSelectedExpression() != null) {
							fieldWithSearchButton.setValue(formExpressionWindow.getSelectedExpression(),
									formExpressionWindow.getSelectedExpression().getName());
							instance.setFormExpression(formExpressionWindow.getSelectedExpression());
							firePropertyUpdateListener(instance);
							AbcdLogger.info(this.getClass().getName(), "User '"
									+ UserSessionHandler.getUser().getEmailAddress() + "' added Expression "
									+ instance.getFormExpression().getName() + " to Calculation node with ID:"
									+ instance.getId() + "'.");
							window.close();
						} else {
							MessageManager.showError(LanguageCodes.ERROR_SELECT_EXPRESSION);
						}
					}
				});
				formExpressionWindow.showCentered();
			}
		});
		fieldWithSearchButton.addRemoveClickListener(new ClickListener() {
			private static final long serialVersionUID = -3314196233359245226L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setFormExpression(null);
				firePropertyUpdateListener(instance);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' removed expression from Calculation node with ID:" + instance.getId() + "'.");
			}
		});

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(fieldWithSearchButton);

		addTab(categoryForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_FORM_NODE_CAPTION), true,
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