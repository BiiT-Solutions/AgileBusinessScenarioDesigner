package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
