package com.biit.abcd.webpages.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * Component for editing an expression. Is composed by a viewer and a properties
 * menu in tabs.
 */
public abstract class TabEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private TabSheet tabMenu;

	public TabEditorComponent() {
		initComponents(false);
	}

	public TabEditorComponent(boolean questionEditor) {
		initComponents(questionEditor);
	}

	public void initComponents(boolean questionEditor) {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		tabMenu = new TabSheet();
		tabMenu.setHeight("100%");

		setCompositionRoot(rootLayout);
	}

	public void setTab(Component component, String description, Resource icon){
		Tab tab = tabMenu.addTab(component);
		tab.setDescription("");
		tab.setIcon(icon);
	}

	public TabSheet getTabSheet(){
		return tabMenu;
	}

	public HorizontalLayout getRootLayout(){
		return rootLayout;
	}
}
