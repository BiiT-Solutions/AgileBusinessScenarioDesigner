package com.biit.abcd.webpages.elements.treetable;

import java.util.HashMap;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class PropertiesContainer extends CustomComponent {

	private static final long serialVersionUID = -4459509560858677005L;
	public static String CLASSNAME = "v-properties-container";
	private VerticalLayout rootLayout;
	private HashMap<Class<?>, PropertiesComponent> propertiesComponents;

	public PropertiesContainer() {

		propertiesComponents = new HashMap<Class<?>, PropertiesComponent>();

		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();
		setStyleName(CLASSNAME);
	}

	public void registerPropertiesComponent(Class<?> classId, PropertiesComponent component) {
		propertiesComponents.put(classId, component);
	}

	public void updatePropertiesComponent(TreeObject value) {
		if (value == null) {
			rootLayout.removeAllComponents();
		} else {
			PropertiesComponent baseObject = propertiesComponents.get(value.getClass());
			try {
				rootLayout.removeAllComponents();
				
				PropertiesComponent newInstance = baseObject.getClass().newInstance();
				newInstance.setElement(value);
				rootLayout.addComponent(newInstance);
				
				rootLayout.markAsDirty();
			} catch (InstantiationException | IllegalAccessException e) {
				MessageManager.showError(ServerTranslate.tr(LanguageCodes.ERROR_UNEXPECTED_ERROR) + " "
						+ ServerTranslate.tr(LanguageCodes.ERROR_CONTACT));
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}
}
