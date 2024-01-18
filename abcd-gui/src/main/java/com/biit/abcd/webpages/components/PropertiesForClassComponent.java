package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public abstract class PropertiesForClassComponent<T> extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private Class<?> type;
	private AccordionMultiple rootAccordion;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	private List<ElementAddedListener> newElementListeners;
	protected TextField createdBy, creationTime, updatedBy, updateTime;
	protected Button clearOriginalReference;

	public PropertiesForClassComponent(Class<? extends T> type) {
		this.type = type;
		propertyUpdateListeners = new ArrayList<>();
		newElementListeners = new ArrayList<>();

		rootAccordion = new AccordionMultiple();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);

		setCompositionRoot(rootAccordion);
		setWidth("100%");
		setHeight(null);

		addDetachListener((DetachListener) event -> {
            focus();
            updateAndExit();
        });
	}

	@Override
	public void detach() {
		if (!isConnectorEnabled()) {
			return;
		}
		super.detach();
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
        for (Component component : container) {
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
			setElementForProperties((T) element);
		}
	}

	protected abstract void setElementForProperties(T element);

	protected abstract void updateElement();

	protected abstract void firePropertyUpdateOnExitListener();

	private void updateAndExit() {
		// Check UI is different of null due to detach is also triggered when UI.close() is called.
		if (UI.getCurrent() != null) {
			updateElement();
			firePropertyUpdateOnExitListener();
		}
	}

	public void addPropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		newElementListeners.add(listener);
	}

	public void removePropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		newElementListeners.remove(listener);
	}

	protected void firePropertyUpdateListener(Object element) {
		for (PropertieUpdateListener listener : propertyUpdateListeners) {
			listener.propertyUpdate(element);
		}
	}

	protected void fireExpressionAddedListener(Object newElement) {
		for (ElementAddedListener listener : newElementListeners) {
			listener.elementAdded(newElement);
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
				if ((field.getValidators() != null) && (!field.getValidators().isEmpty())) {
					Collection<Validator> validators = field.getValidators();
					for (Validator validator : validators) {
						try {
							validator.validate(field.getValue());
							updateElement();
						} catch (InvalidValueException e) {
							AbcdLogger.warning(this.getClass().getName(), e.toString());
						}
					}
				} else {
					updateElement();
				}
			}
		}
	};
}
