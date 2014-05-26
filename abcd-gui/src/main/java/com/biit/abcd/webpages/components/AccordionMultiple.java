package com.biit.abcd.webpages.components;

import java.util.Collection;
import java.util.HashMap;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AccordionMultiple extends CustomComponent {
	private static final long serialVersionUID = 5129178526197426807L;
	private static final String CLASSNAME = "v-accordion-multiple";

	private VerticalLayout rootLayout;
	private HashMap<Component, AccordionTab> componentToTabComponent;

	public AccordionMultiple() {
		componentToTabComponent = new HashMap<>();

		rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		setCompositionRoot(rootLayout);
		setWidth("100%");
		setHeight(null);
		setStyleName(CLASSNAME);
	}

	public void addTab(Component component, String caption,boolean toggle) {
		AccordionTab tab = createTab(component, caption);
		if(toggle){
			tab.toggle();
		}
		rootLayout.addComponent(tab);
	}

	public void addTab(Component component, String caption,boolean toggle, int index) {
		AccordionTab tab = createTab(component, caption);
		if(toggle){
			tab.toggle();
		}
		rootLayout.addComponent(tab, index);
	}
	
	public Collection<Component> getAllComponents(){
		return componentToTabComponent.keySet();
	}

	public void removeTab(Component component) {
		AccordionTab tab = componentToTabComponent.get(component);
		componentToTabComponent.remove(component);
		rootLayout.removeComponent(tab);
	}

	private AccordionTab createTab(Component component, String caption) {
		AccordionTab tab = new AccordionTab(component, caption);
		componentToTabComponent.put(component, tab);
		return tab;
	}

	private class AccordionTab extends CustomComponent implements LayoutClickListener {
		private static final long serialVersionUID = 3108983922974101784L;
		private static final String CLASSNAME = "v-accordion-multiple-tab";
		private static final String TAB_CLASSNAME = "v-tab-bar";
		private static final String ICON_SIZE = "11px";
		private VerticalLayout rootLayout;
		private Component userComponent;
		private HorizontalLayout bar;
		private Image expandImage;
		private Image collapseImage;

		public AccordionTab(Component component, String caption) {
			rootLayout = new VerticalLayout();
			rootLayout.setWidth("100%");
			rootLayout.setHeight(null);

			setCompositionRoot(rootLayout);
			setWidth("100%");
			setHeight(null);
			setStyleName(CLASSNAME);

			userComponent = component;

			rootLayout.addComponent(createTabBar(caption));
		}

		private HorizontalLayout createTabBar(String caption) {
			expandImage = new Image(null, ThemeIcons.EXPAND.getThemeResource());
			expandImage.setWidth(ICON_SIZE);
			expandImage.setHeight(ICON_SIZE);
			collapseImage = new Image(null, ThemeIcons.COLLAPSE.getThemeResource());
			collapseImage.setWidth(ICON_SIZE);
			collapseImage.setHeight(ICON_SIZE);

			bar = new HorizontalLayout();
			Label captionLabel = new Label(caption);
			captionLabel.setWidth(null);
			bar.addComponent(expandImage);
			bar.setExpandRatio(expandImage, 0.0f);
			bar.addComponent(captionLabel);
			bar.setExpandRatio(captionLabel, 1.0f);
			bar.addLayoutClickListener(this);
			bar.setWidth("100%");
			bar.setHeight("32px");
			bar.setStyleName(TAB_CLASSNAME);
			return bar;
		}

		@Override
		public void layoutClick(LayoutClickEvent event) {
			toggle();
		}
		
		public void toggle(){
			if (rootLayout.getComponentIndex(userComponent) == -1) {
				rootLayout.addComponent(userComponent);
				rootLayout.setComponentAlignment(userComponent, Alignment.TOP_CENTER);
			} else {
				rootLayout.removeComponent(userComponent);
			}
			if (bar.getComponentIndex(expandImage) == -1) {
				bar.removeComponent(collapseImage);
				bar.addComponentAsFirst(expandImage);
				bar.setExpandRatio(expandImage, 0.0f);
			} else {
				bar.removeComponent(expandImage);
				bar.addComponentAsFirst(collapseImage);
				bar.setExpandRatio(collapseImage, 0.0f);
			}
		}
	}
}
