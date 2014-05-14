package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.vaadin.ui.TextField;

public class QuestionProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Question instance;
	private TextField groupTechnicalLabel;
	private TextField elementCreatedBy;
	private TextField elementCreationTime;
	private TextField elementUpdatedBy;
	private TextField elementUpdateTime;

	public QuestionProperties() {
	}

	@Override
	public void setElement(TreeObject element) {
		instance = (Question) element;

		groupTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.setValue(instance.getTechnicalName());

		String createdBy = "";
		String updatedBy = "";
		try {
			createdBy = instance.getCreatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(instance.getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			createdBy = instance.getCreatedBy() + "";
		}

		try {
			updatedBy = instance.getUpdatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(instance.getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			updatedBy = instance.getUpdatedBy() + "";
		}

		String creationTime = instance.getCreationTime() == null ? "" : instance.getCreationTime().toString();
		String updatedTime = instance.getUpdateTime() == null ? "" : instance.getUpdateTime().toString();
		elementCreatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		elementCreatedBy.setValue(createdBy);
		elementCreationTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		elementCreationTime.setValue(creationTime);
		elementUpdatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		elementUpdatedBy.setValue(updatedBy);
		elementUpdateTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		elementUpdateTime.setValue(updatedTime);

		addFormField(groupTechnicalLabel);
		addFormField(elementCreatedBy);
		addFormField(elementCreationTime);
		addFormField(elementUpdatedBy);
		addFormField(elementUpdateTime);
	}

	@Override
	public void updateElement() {
		instance.setTechnicalName(groupTechnicalLabel.getValue());
		instance.setUpdatedBy(UserSessionHandler.getUser());
		instance.setUpdateTime();
		firePropertyUpdateListener(instance);
	}

}