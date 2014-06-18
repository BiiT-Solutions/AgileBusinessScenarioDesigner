package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;

public abstract class PropertiesForClassComponent<T> extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private Class<?> type;
	private AccordionMultiple rootAccordion;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	protected TextField createdBy, creationTime, updatedBy, updateTime;

	public PropertiesForClassComponent(Class<? extends T> type) {
		this.type = type;
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();

		rootAccordion = new AccordionMultiple();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);

		setCompositionRoot(rootAccordion);
		setWidth("100%");
		setHeight(null);

		addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -1283647887712898710L;

			@Override
			public void detach(DetachEvent event) {
				focus();
				update();
			}
		});
	}

	public void addTab(Component component, String caption, boolean toggle) {
		rootAccordion.addTab(component, caption, toggle);
		inspectComponentAndAddValueChangeListeners(component);
	}

	public void addTab(Component component, String caption, boolean toggle, int index) {
		rootAccordion.addTab(component, caption, toggle, index);
		inspectComponentAndAddValueChangeListeners(component);
	}

	private void inspectComponentAndAddValueChangeListeners(Component component) {
		if (component instanceof AbstractComponentContainer) {
			addValueChangeListenerToFieldsInContainer((AbstractComponentContainer) component);
		} else {
			if (component instanceof AbstractField<?>) {
				addValueChangeListenerToField((AbstractField<?>) component);
			}
		}
	}

	private void addValueChangeListenerToFieldsInContainer(AbstractComponentContainer container) {
		Iterator<Component> itr = container.iterator();
		while (itr.hasNext()) {
			Component component = itr.next();
			if (component instanceof AbstractComponentContainer) {
				addValueChangeListenerToFieldsInContainer(container);
			} else {
				if (component instanceof AbstractField<?>) {
					addValueChangeListenerToField((AbstractField<?>) component);
				}
			}
		}
	}

	private void addValueChangeListenerToField(AbstractField<?> component) {
		// if(field.isEnabled())
		component.setImmediate(true);
		component.addValueChangeListener(new FieldValueChangeListener(component));
	}

	@SuppressWarnings("unchecked")
	public void setElement(Object element) {
		if (type.isInstance(element)) {
			setElementAbstract((T) element);
		}
	}

	protected abstract void setElementAbstract(T element);

	protected abstract void updateElement();

	protected abstract void firePropertyUpdateListener();

	private void update() {
		updateElement();
		firePropertyUpdateListener();
	}

	public void addPropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void removePropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	protected void firePropertyUpdateListener(Object element) {
		for (PropertieUpdateListener listener : propertyUpdateListeners) {
			listener.propertyUpdate(element);
		}
	}

	public Class<?> getUnderlyingClass() {
		return type;
	}

	private class FieldValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = -5503553212373718399L;

		AbstractField<?> field;

		public FieldValueChangeListener(AbstractField<?> field) {
			this.field = field;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (field.isAttached() && field.isEnabled()) {
				update();
			}
		}
	};
}
