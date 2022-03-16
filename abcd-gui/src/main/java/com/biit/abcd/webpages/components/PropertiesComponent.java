package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class PropertiesComponent extends CustomComponent implements Component.Focusable {

	private static final long serialVersionUID = -4459509560858677005L;
	public static final String CLASSNAME = "v-properties-container";
	private VerticalLayout rootLayout;
	private HashMap<Class<?>, PropertiesForClassComponent<?>> propertiesComponents;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	private List<ElementAddedListener> elementAddedListener;
	private boolean fireListeners;

	public PropertiesComponent() {
		propertiesComponents = new HashMap<Class<?>, PropertiesForClassComponent<?>>();
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();
		elementAddedListener = new ArrayList<ElementAddedListener>();
		fireListeners = true;

		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setStyleName(CLASSNAME);
		setCompositionRoot(rootLayout);
		setSizeFull();
		setStyleName(CLASSNAME);
	}

	public void registerPropertiesComponent(PropertiesForClassComponent<?> component) {
		propertiesComponents.put(component.getUnderlyingClass(), component);
	}

	public PropertiesForClassComponent<?> getCurrentDisplayedProperties() {
		if (rootLayout.getComponentCount() > 0) {
			return (PropertiesForClassComponent<?>) rootLayout.getComponent(0);
		}
		return null;
	}

	public void updatePropertiesComponent(Object value) {
		if (value == null) {
			rootLayout.removeAllComponents();
		} else {
			rootLayout.removeAllComponents();
			PropertiesForClassComponent<?> baseObject = propertiesComponents.get(value.getClass());

			if (baseObject == null) {
				return;
			}

			try {
				PropertiesForClassComponent<?> newInstance = baseObject.getClass().newInstance();
				newInstance.setElement(value);
				newInstance.addPropertyUpdateListener(new PropertieUpdateListener() {
					@Override
					public void propertyUpdate(Object element) {
						firePropertyUpdateListener(element);
					}
				});
				newInstance.addNewElementListener(new ElementAddedListener() {

					@Override
					public void elementAdded(Object newElement) {
						fireElementAddedListener(newElement);
					}
				});
				rootLayout.addComponent(newInstance);
				rootLayout.markAsDirty();
			} catch (InstantiationException | IllegalAccessException e) {
				MessageManager.showError(ServerTranslate.translate(LanguageCodes.ERROR_UNEXPECTED_ERROR) + " "
						+ ServerTranslate.translate(LanguageCodes.ERROR_CONTACT));
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	public void addPropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		elementAddedListener.add(listener);
	}

	public void removePropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		elementAddedListener.remove(listener);
	}

	protected void firePropertyUpdateListener(Object element) {
		if (fireListeners && UI.getCurrent() != null) {
			for (PropertieUpdateListener listener : propertyUpdateListeners) {
				listener.propertyUpdate(element);
			}
		}
	}

	protected void fireElementAddedListener(Object element) {
		for (ElementAddedListener listener : elementAddedListener) {
			listener.elementAdded(element);
		}
	}

	public void removeAllPropertyUpdateListeners() {
		propertyUpdateListeners.clear();
	}

	@Override
	public void focus() {
		super.focus();
	}

	@Override
	public int getTabIndex() {
		return 0;

	}

	@Override
	public void setTabIndex(int tabIndex) {
		// Does nothing
	}

	public void setFireListeners(boolean fireListeners) {
		this.fireListeners = fireListeners;
	}
}
