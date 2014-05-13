package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.jointjs.diagram.builder.server.DiagramBuilder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormDiagramBuilder extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private DiagramBuilder diagramBuilder;
	private FormDiagramBuilderUpperMenu diagramBuilderUpperMenu;

	private Form form;

	public FormDiagramBuilder() {
		diagramBuilder = new DiagramBuilder();
		diagramBuilder.setSizeFull();
		getWorkingAreaLayout().addComponent(diagramBuilder);
		
		
		initUpperMenu();
	}
	
	private void initUpperMenu(){
		diagramBuilderUpperMenu = new FormDiagramBuilderUpperMenu();
		diagramBuilderUpperMenu.addClearButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3419227544702101097L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.clear();
			}
		});
		diagramBuilderUpperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3692380302089994511L;

			@Override
			public void buttonClick(ClickEvent event) {
				//Do nothing
				MessageManager.showInfo("Function not implemented yet");
			}
		});
		diagramBuilderUpperMenu.addUndoButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4071103244551097590L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.undo();
			}
		});
		diagramBuilderUpperMenu.addRedoButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -8948171519257161439L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.redo();
			}
		});
		diagramBuilderUpperMenu.addToFrontButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 10874132205961162L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.toFront();
			}
		});
		diagramBuilderUpperMenu.addToBackButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6793636700517664421L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.toBack();
			}
		});
		diagramBuilderUpperMenu.addToSvgButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6867911525454122263L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.openAsSvg();
			}
		});
		diagramBuilderUpperMenu.addToPngButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -2961691826072098281L;

			@Override
			public void buttonClick(ClickEvent event) {
				diagramBuilder.openAsPng();
			}
		});
		
		setUpperMenu(diagramBuilderUpperMenu);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

}