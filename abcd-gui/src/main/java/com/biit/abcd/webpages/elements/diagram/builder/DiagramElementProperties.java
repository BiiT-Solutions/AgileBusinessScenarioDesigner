package com.biit.abcd.webpages.elements.diagram.builder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class DiagramElementProperties extends PropertiesForClassComponent<DiagramElement> {
	private static final long serialVersionUID = -5764645910674916633L;

	private DiagramElement instance;
	private TextField diagramElementLabel;

	public DiagramElementProperties() {
		super(DiagramElement.class);
	}

	@Override
	public void setElementForProperties(DiagramElement element) {
		instance = element;

		diagramElementLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		if (instance.getText() == null) {
			instance.setText(new DiagramText());
		}
		diagramElementLabel.setValue(instance.getText().getText());

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(diagramElementLabel);

		addTab(categoryForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION), true, 0);
	}

	@Override
	public void updateElement() {
		instance.getText().setText(diagramElementLabel.getValue());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}
