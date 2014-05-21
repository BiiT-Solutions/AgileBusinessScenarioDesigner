package com.biit.abcd.webpages.elements.diagramBuilder;

import com.biit.jointjs.diagram.builder.server.ElementPickedListener;

public class DiagramBuilderElementPicked implements ElementPickedListener {

	private JsonPropertiesComponent propertiesComponent;

	public DiagramBuilderElementPicked(JsonPropertiesComponent propertiesComponent) {
		this.propertiesComponent = propertiesComponent;
	}

	@Override
	public void nodePickedListener(String jsonString) {
		System.out.println("Node picker: " + jsonString);
	}

	@Override
	public void connectionPickedListener(String jsonString) {
		System.out.println("Connection picker: " + jsonString);
	}

}
