package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class PropertiesForClassComponent<T> extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private T type;
	protected AccordionMultiple rootAccordion;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	protected TextField createdBy, creationTime, updatedBy, updateTime;

	public PropertiesForClassComponent(T type) {
		this.type = type;
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();

		rootAccordion = new AccordionMultiple();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);

		setCompositionRoot(rootAccordion);
		setWidth("100%");
		setHeight(null);
	}

	protected void addValueChangeListenerToFormComponents(FormLayout formLayout) {
		Iterator<Component> itr = formLayout.iterator();
		while (itr.hasNext()) {
			Component formComponent = itr.next();
			if (formComponent instanceof AbstractField<?>) {
				if (formComponent.isEnabled()) {
					addValueChangeListenerToField((AbstractField<?>) formComponent);
				}
			}
		}
	}

	private void addValueChangeListenerToField(AbstractField<?> component) {
		component.setImmediate(true);
		component.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5503553212373718399L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateElement();
			}
		});
	}

	@SuppressWarnings({ "unchecked" })
	public void setElement(Object element) {
		if (type.getClass().isInstance(element)) {
			setElementAbstract((T) element);
		}
	}

	public abstract void setElementAbstract(T element);

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

	public AccordionMultiple getRootAccordion() {
		return rootAccordion;
	}

	@SuppressWarnings("unchecked")
	public T getUnderlyingType() {
		try {
			return (T) type.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return type;
	}
}
