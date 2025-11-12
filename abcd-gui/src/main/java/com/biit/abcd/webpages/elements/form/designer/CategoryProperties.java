package com.biit.abcd.webpages.elements.form.designer;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.webpages.elements.form.designer.validators.ValidatorDuplicateNameOnSameTreeObjectLevel;
import com.biit.abcd.webpages.elements.form.designer.validators.ValidatorTreeObjectName;
import com.biit.abcd.webpages.elements.form.designer.validators.ValidatorTreeObjectNameLength;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class CategoryProperties extends SecuredFormElementProperties<Category> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Category instance;
	private TextField categoryLabel;
	private final String TECHNICAL_NAME_VALIDATOR_REGEX = "([A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff_])([0-9A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff\\_\\xb7]){2,}";

	public CategoryProperties() {
		super(Category.class);
	}

	@Override
	public void setElementForProperties(Category element) {
		instance = element;

		categoryLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		categoryLabel.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		categoryLabel.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		categoryLabel.addValidator(new ValidatorTreeObjectNameLength());
		categoryLabel.addValidator(new RegexpValidator(TECHNICAL_NAME_VALIDATOR_REGEX, ServerTranslate
				.translate(LanguageCodes.TECHNICAL_NAME_ERROR)));
		categoryLabel.setValue(instance.getName());

		FormLayout categoryFormLayout = new FormLayout();
		categoryFormLayout.setWidth(null);
		categoryFormLayout.addComponent(categoryLabel);

		addTab(categoryFormLayout,
				ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		if (categoryLabel.isValid()) {
			String instanceName = instance.getName();
			// To avoid setting repeated values
			if (!categoryLabel.getValue().equals(instanceName)) {
				try {
					instance.setName(categoryLabel.getValue());
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Category '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "'.");
				} catch (FieldTooLongException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
							LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
					try {
						try {
							instance.setName(categoryLabel.getValue().substring(0, 185));
							AbcdLogger.info(this.getClass().getName(), "User '"
									+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Category '"
									+ instanceName + "' property 'Name' to '" + instance.getName()
									+ "' (Name too long).");
						} catch (CharacterNotAllowedException e1) {
							MessageManager.showWarning(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS, 
									LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, instance.getName(), instance.getSimpleAsciiName() );
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
							LanguageCodes.WARNING_NAME_INVALID_CHARACTERS, LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, 
											instance.getName(), instance.getSimpleAsciiName() );
					try {
						instance.setName(instance.getSimpleAsciiName());
					} catch (FieldTooLongException | CharacterNotAllowedException e1) {
						// Impossible.
					}
				}
			}
		}
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(categoryLabel));
	}

}
