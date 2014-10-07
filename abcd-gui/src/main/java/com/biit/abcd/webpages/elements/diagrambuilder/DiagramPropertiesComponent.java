package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class DiagramPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 7006610652322714314L;

	public DiagramPropertiesComponent() {
		super();
		registerPropertiesComponent(new DiagramPropertiesSource());
		registerPropertiesComponent(new DiagramPropertiesSink());
		registerPropertiesComponent(new DiagramPropertiesFork());
		registerPropertiesComponent(new DiagramPropertiesDiagramChild());
		registerPropertiesComponent(new DiagramPropertiesTable());
		registerPropertiesComponent(new DiagramPropertiesCalculation());
		registerPropertiesComponent(new DiagramPropertiesLink());
		registerPropertiesComponent(new DiagramPropertiesRule());
	}
}