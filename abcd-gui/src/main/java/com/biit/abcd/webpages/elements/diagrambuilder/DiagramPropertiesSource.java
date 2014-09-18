package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramBiitText;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class DiagramPropertiesSource extends PropertiesForClassComponent<DiagramSource> {
	private static final long serialVersionUID = -5894964889869328279L;
	private DiagramElement instance;
	private TextField diagramElementLabel;

	public DiagramPropertiesSource() {
		super(DiagramSource.class);
	}

	@Override
	public void setElementForProperties(DiagramSource element) {
		instance = element;

		diagramElementLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		if (instance.getBiitText() == null) {
			instance.setBiitText(new DiagramBiitText());
		}
		diagramElementLabel.setValue(instance.getBiitText().getText());

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(diagramElementLabel);

		addTab(categoryForm, ServerTranslate
				.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SOURCE_NODE_CAPTION), true, 0);
	}

	@Override
	public void updateElement() {
		instance.getBiitText().setText(diagramElementLabel.getValue());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

}