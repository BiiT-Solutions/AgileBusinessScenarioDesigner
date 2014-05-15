package com.biit.abcd.webpages.elements.treetable;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;

public abstract class PropertiesComponent extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private Accordion rootAccordion;
	private FormLayout formLayout;
	private List<PropertieUpdateListener> propertyUpdateListeners;

	public PropertiesComponent() {
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();
		
		rootAccordion = new Accordion();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);
		
		formLayout = new FormLayout();
		formLayout.setImmediate(true);
		
		rootAccordion.addTab(formLayout,"Kiwi-1");
		rootAccordion.addTab(new VerticalLayout(),"kiwi-2");
		rootAccordion.addTab(new VerticalLayout(),"kiwi-3");
		rootAccordion.addTab(new VerticalLayout(),"kiwi-4");
		rootAccordion.addTab(new VerticalLayout(),"kiwi-5");
		
		setCompositionRoot(rootAccordion);
		setSizeFull();
	}
	
	protected void addFormField(AbstractField<?> component){
		component.setImmediate(true);
		component.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5503553212373718399L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateElement();
			}
		});
		formLayout.addComponent(component);
	}
	
	public abstract void setElement(TreeObject element);

	public abstract void updateElement();

	public void addPropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void removePropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	protected void firePropertyUpdateListener(TreeObject element) {
		for (PropertieUpdateListener listener : propertyUpdateListeners) {
			listener.propertyUpdate(element);
		}
	}
}
