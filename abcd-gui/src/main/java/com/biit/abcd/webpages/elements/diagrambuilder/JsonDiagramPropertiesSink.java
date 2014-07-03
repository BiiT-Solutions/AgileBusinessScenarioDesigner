package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.FieldWithSearchButton;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.components.SelectExpressionWindow;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class JsonDiagramPropertiesSink extends PropertiesForClassComponent<DiagramSink> {
	private static final long serialVersionUID = -5894964889869328279L;
	private DiagramSink instance;
	private FieldWithSearchButton fieldWithSearchButton;

	public JsonDiagramPropertiesSink() {
		super(DiagramSink.class);
	}

	@Override
	public void setElementAbstract(DiagramSink element) {
		instance = element;

		fieldWithSearchButton = new FieldWithSearchButton(
				ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SINK_EXPRESSION_CAPTION));
		fieldWithSearchButton.setNullCaption(ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SINK_EXPRESSION_NULL_CAPTION));
		fieldWithSearchButton.setValue(null);
		if (instance.getFormExpression() != null) {
			fieldWithSearchButton.setValue(instance.getFormExpression(), instance.getFormExpression().getName());
		}
		fieldWithSearchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 8612691233380693806L;

			@Override
			public void buttonClick(ClickEvent event) {
				final SelectExpressionWindow formExpressionWindow = new SelectExpressionWindow();
				formExpressionWindow.addAcceptAcctionListener(new AcceptActionListener() {

					@Override
					public void acceptAction(AcceptCancelWindow window) {
						if (formExpressionWindow.getSelectedExpression() != null) {
							fieldWithSearchButton.setValue(formExpressionWindow.getSelectedExpression(),
									formExpressionWindow.getSelectedExpression().getName());
							instance.setFormExpression(formExpressionWindow.getSelectedExpression());
							instance.getBiitText().setText(instance.getFormExpression().getExpressionTableString());
							firePropertyUpdateListener(instance);
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
			private static final long serialVersionUID = -222450247941571934L;

			@Override
			public void buttonClick(ClickEvent event) {
				instance.setFormExpression(null);
				instance.getBiitText().setText("Sink");
				firePropertyUpdateListener(instance);
			}
		});

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(fieldWithSearchButton);

		addTab(categoryForm, "TODO - JsonDiagramProperties Sink", true, 0);
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