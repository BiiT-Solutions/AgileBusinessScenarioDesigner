package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class JsonDiagramLinkProperties extends PropertiesForClassComponent<DiagramLink> {
	private static final long serialVersionUID = 6308407654774598230L;
	private DiagramLink instance;
	private TextField diagramElementLabel;

	public JsonDiagramLinkProperties() {
		super(DiagramLink.class);
	}

	@Override
	public void setElementAbstract(DiagramLink element) {
		instance = element;

		diagramElementLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		if (instance.getText() == null) {
			diagramElementLabel.setValue("");
		} else {
			diagramElementLabel.setValue(instance.getText());
		}

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(diagramElementLabel);

		addTab(categoryForm, ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION), true, 0);
	}

	@Override
	public void updateElement() {
		instance.setText(diagramElementLabel.getValue());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}