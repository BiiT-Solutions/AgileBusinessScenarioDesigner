package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class JsonPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 7006610652322714314L;

	public JsonPropertiesComponent() {
		super();
		registerPropertiesComponent(new JsonDiagramPropertiesSource());
		registerPropertiesComponent(new JsonDiagramPropertiesSink());
		registerPropertiesComponent(new JsonDiagramPropertiesFork());
		registerPropertiesComponent(new JsonDiagramPropertiesTable());
		registerPropertiesComponent(new JsonDiagramPropertiesCalculation());
		registerPropertiesComponent(new JsonDiagramPropertiesLink());
	}
}