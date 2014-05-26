package com.biit.abcd.webpages.elements.diagramBuilder;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.jointjs.diagram.builder.server.ElementPickedListener;

public class DiagramBuilderElementPicked implements ElementPickedListener {

	private JsonPropertiesComponent propertiesComponent;

	public DiagramBuilderElementPicked(JsonPropertiesComponent propertiesComponent) {
		this.propertiesComponent = propertiesComponent;
	}

	@Override
	public void nodePickedListener(String jsonString) {
		propertiesComponent.focus();
		DiagramElement element = DiagramElement.fromJson(jsonString);
		propertiesComponent.updatePropertiesComponent(element);
	}

	@Override
	public void connectionPickedListener(String jsonString) {
		DiagramLink element = DiagramLink.fromJson(jsonString);
		propertiesComponent.updatePropertiesComponent(element);
	}

}
