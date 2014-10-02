package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Group;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class GroupProperties extends GenericFormElementProperties<Group> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Group instance;
	private TextField groupTechnicalLabel;
	private CheckBox groupIsRepeatable;

	public GroupProperties() {
		super(Group.class);
	}

	@Override
	public void setElementForProperties(Group element) {
		instance = element;
		groupTechnicalLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.setValue(instance.getName());
		groupIsRepeatable = new CheckBox(ServerTranslate.translate(LanguageCodes.GROUP_PROPERTIES_REPEAT));
		groupIsRepeatable.setValue(instance.isRepeatable());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(groupTechnicalLabel);
		answerForm.addComponent(groupIsRepeatable);

		addTab(answerForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_GROUP_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		String instanceName = instance.getName();
		try {
			instance.setName(groupTechnicalLabel.getValue());
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has modified the Group '" + instanceName + "' property 'Name' to '" + instance.getName()
					+ "'.");
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				try {
					instance.setName(groupTechnicalLabel.getValue().substring(0, 185));
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Group '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "' (Name too long).");
				} catch (CharacterNotAllowedException e1) {
					MessageManager.showWarning(
							ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate
									.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
											instance.getName(), instance.getSimpleAsciiName() }));
					try {
						instance.setName(instance.getSimpleAsciiName());
					} catch (CharacterNotAllowedException e2) {
						// Impossible.
					}
				}
			} catch (FieldTooLongException e1) {
				// Impossible.
			}
		} catch (CharacterNotAllowedException e) {
			MessageManager.showWarning(
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS),
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
							instance.getName(), instance.getSimpleAsciiName() }));
			try {
				instance.setName(instance.getSimpleAsciiName());
			} catch (FieldTooLongException | CharacterNotAllowedException e1) {
				// Impossible.
			}
		}
		instance.setRepeatable(groupIsRepeatable.getValue());
		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "'Group '" + instance.getName() + "' value 'Repeat' set to '" + groupIsRepeatable.getValue() + "'.");

		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}
}