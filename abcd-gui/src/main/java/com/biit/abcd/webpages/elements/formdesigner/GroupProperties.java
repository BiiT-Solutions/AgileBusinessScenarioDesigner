package com.biit.abcd.webpages.elements.formdesigner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorDuplicateNameOnSameTreeObjectLevel;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectName;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectNameLength;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class GroupProperties extends SecuredFormElementProperties<Group> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Group instance;
	private TextField groupTechnicalLabel;
	private CheckBox groupIsRepeatable;
	private final String TECHNICAL_NAME_VALIDATOR_REGEX = "([A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff_])([0-9A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff\\_\\xb7]){2,}";

	public GroupProperties() {
		super(Group.class);
	}

	@Override
	public void setElementForProperties(Group element) {
		instance = element;
		groupTechnicalLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		groupTechnicalLabel.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		groupTechnicalLabel.addValidator(new ValidatorTreeObjectNameLength());
		groupTechnicalLabel.setValue(instance.getName());
		groupTechnicalLabel.addValidator(new RegexpValidator(TECHNICAL_NAME_VALIDATOR_REGEX, ServerTranslate
				.translate(LanguageCodes.TECHNICAL_NAME_ERROR)));

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
		if (groupTechnicalLabel.isValid()) {
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
						MessageManager.showWarning(ServerTranslate
								.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate.translate(
								LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION,
								new Object[] { instance.getName(), instance.getSimpleAsciiName() }));
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
				MessageManager.showWarning(ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS),
						ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION,
								new Object[] { instance.getName(), instance.getSimpleAsciiName() }));
				try {
					instance.setName(instance.getSimpleAsciiName());
				} catch (FieldTooLongException | CharacterNotAllowedException e1) {
					// Impossible.
				}
			}
			instance.setRepeatable(groupIsRepeatable.getValue());
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "'Group '" + instance.getName() + "' value 'Repeat' set to '" + groupIsRepeatable.getValue()
					+ "'.");

			// firePropertyUpdateListener(getTreeObjectInstance());
		}
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(groupTechnicalLabel, groupIsRepeatable));
	}

	private boolean existTestScenariosLinked() {
		List<TestScenario> testScenarios = UserSessionHandler.getTestScenariosController().getTestScenarios(
				UserSessionHandler.getFormController().getForm());
		return !testScenarios.isEmpty();
	}
}