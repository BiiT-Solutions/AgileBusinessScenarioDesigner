package com.biit.abcd.webpages.elements.diagram.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class DiagramPropertiesSource extends SecuredDiagramElementProperties<DiagramSource> {
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
		if (instance.getText() == null) {
			instance.setText(new DiagramText());
		}
		diagramElementLabel.setValue(instance.getText().getText());

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(diagramElementLabel);

		addTab(categoryForm, ServerTranslate.translate(LanguageCodes.JSON_DIAGRAM_PROPERTIES_SOURCE_NODE_CAPTION),
				true, 0);
	}

	@Override
	public void updateElement() {
		instance.getText().setText(diagramElementLabel.getValue());
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(instance);
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(diagramElementLabel));
	}

}