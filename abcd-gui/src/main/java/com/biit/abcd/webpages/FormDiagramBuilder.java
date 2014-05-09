package com.biit.abcd.webpages;

import com.biit.abcd.component.WebPageComponent;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class FormDiagramBuilder extends WebPageComponent{

	private static final long serialVersionUID = 3237410805898133935L;
	
	private VerticalLayout rootLayout;
	private DiagramBuilder diagramBuilder;
	
	public FormDiagramBuilder(){
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();
		
		diagramBuilder = new DiagramBuilder();
		diagramBuilder.setSizeFull();
		rootLayout.addComponent(diagramBuilder);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}