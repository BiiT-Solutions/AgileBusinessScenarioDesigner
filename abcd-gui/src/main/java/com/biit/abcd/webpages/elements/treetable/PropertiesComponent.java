package com.biit.abcd.webpages.elements.treetable;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.AccordionMultiple;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class PropertiesComponent extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private AccordionMultiple rootAccordion;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	protected TextField createdBy, creationTime, updatedBy, updateTime;

	public PropertiesComponent() {
		propertyUpdateListeners = new ArrayList<PropertieUpdateListener>();

		rootAccordion = new AccordionMultiple();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);

		setCompositionRoot(rootAccordion);
		setWidth("100%");
		setHeight(null);

		initCommonProperties();
	}

	protected void initCommonProperties() {
		createdBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdBy.setEnabled(false);
		creationTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTime.setEnabled(false);
		updatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedBy.setEnabled(false);
		updateTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTime.setEnabled(false);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdBy);
		commonProperties.addComponent(creationTime);
		commonProperties.addComponent(updatedBy);
		commonProperties.addComponent(updateTime);

		rootAccordion.addTab(commonProperties,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION));
	}

	protected void initCommonPropertiesValues(TreeObject element) {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";
		try {
			valueCreatedBy = element.getCreatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(element.getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueCreatedBy = element.getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = element.getUpdatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(element.getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueUpdatedBy = element.getUpdatedBy() + "";
		}

		String valueCreationTime = element.getCreationTime() == null ? "" : element.getCreationTime().toString();
		String valueUpdatedTime = element.getUpdateTime() == null ? "" : element.getUpdateTime().toString();

		createdBy.setValue(valueCreatedBy);
		creationTime.setValue(valueCreationTime);
		updatedBy.setValue(valueUpdatedBy);
		updateTime.setValue(valueUpdatedTime);
	}

	protected void addValueChangeListenerToField(AbstractField<?> component) {
		component.setImmediate(true);
		component.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5503553212373718399L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateElement();
			}
		});
	}

	public void setElement(TreeObject element) {
		setElementAbstract(element);
		initCommonPropertiesValues(element);
	}

	public abstract void setElementAbstract(TreeObject element);

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
}
