package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class PropertiesComponent extends CustomComponent {

	private static final long serialVersionUID = -4459509560858677005L;
	public static String CLASSNAME = "v-properties-container";
	private VerticalLayout rootLayout;
	private HashMap<Class<?>, PropertiesForClassComponent<?>> propertiesComponents;
	private List<PropertieUpdateListener> propertyUpdateListeners;

	public PropertiesComponent() {

		propertiesComponents = new HashMap<Class<?>, PropertiesForClassComponent<?>>();
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();

		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setStyleName(CLASSNAME);
		setCompositionRoot(rootLayout);
		setSizeFull();
		setStyleName(CLASSNAME);
	}

	public void registerPropertiesComponent(PropertiesForClassComponent<?> component) {
		propertiesComponents.put(component.getUnderlyingType().getClass(), component);
	}

	public void updatePropertiesComponent(TreeObject value) {
		if (value == null) {
			rootLayout.removeAllComponents();
		} else {
			PropertiesForClassComponent<?> baseObject = propertiesComponents.get(value.getClass());
			try {
				rootLayout.removeAllComponents();

				PropertiesForClassComponent<?> newInstance = baseObject.getClass().newInstance();
				newInstance.setElement(value);
				newInstance.addPropertyUpdateListener(new PropertieUpdateListener() {
					@Override
					public void propertyUpdate(Object element) {
						firePropertyUpdateListener(element);
					}
				});
				rootLayout.addComponent(newInstance);

				rootLayout.markAsDirty();
			} catch (InstantiationException | IllegalAccessException e) {
				MessageManager.showError(ServerTranslate.tr(LanguageCodes.ERROR_UNEXPECTED_ERROR) + " "
						+ ServerTranslate.tr(LanguageCodes.ERROR_CONTACT));
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
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
}
