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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.WebMap;
import com.biit.usermanager.entity.IUser;
import com.vaadin.ui.Button;

/**
 * Add permissions to enable buttons. A form must be previously selected.
 *
 */
public abstract class SecuredMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = -5461554340968281793L;
	private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
			Arrays.asList(AbcdActivity.FORM_EDITING));
	private Set<Button> disabledButtons = null;

	protected SecuredMenu() {
		super();
	}

	private Set<Button> calculateDisabledButtons() {
		Set<Button> disabledButtons = new HashSet<>();
		IUser<Long> user = UserSessionHandler.getUser();
		// Check permissions.
		boolean editionEnabled = false;
		boolean inUse = true;
		if (user == null || UserSessionHandler.getFormController() == null
				|| UserSessionHandler.getFormController().getForm() == null) {
			AbcdLogger.info(this.getClass().getName(), "User redirected to login screen.");
			ApplicationFrame.navigateTo(WebMap.getLoginPage());
		} else {
			// Form is not in use.
			if (!getSecurityService()
					.isFormAlreadyInUse(UserSessionHandler.getFormController().getForm().getId(), user)) {
				inUse = false;
				// user has permissions to edit this form.
				if (!getSecurityService().isFormReadOnly(UserSessionHandler.getFormController().getForm(), user)) {
					editionEnabled = true;
				}
			}
		}

		// Disable all Buttons
		if (!editionEnabled) {
			if (inUse) {
				MessageManager.showWarning(LanguageCodes.WARNING_FORM_IN_USE,
						LanguageCodes.WARNING_FORM_IN_USE_DESCRIPTION);
				// In use, no buttons can be used.
				for (Button button : getButtons()) {
					disabledButtons.add(button);
				}
			} else {
				if (!getSecuredButtons().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.WARNING_FORM_READ_ONLY,
							LanguageCodes.WARNING_FORM_READ_ONLY_DESCRIPTION);
					// Read only, some buttons are disabled, others allowed.
					for (Button button : getSecuredButtons()) {
						disabledButtons.add(button);
					}
				}
			}
		}
		return disabledButtons;
	}

	public abstract Set<Button> getSecuredButtons();

	/**
	 * Return a list of buttons that must be disabled due to user permissions
	 * limitations.
	 *
	 * @return a set of buttons that are disabled.
	 */
	public Set<Button> getDisabledButtons() {
		if (disabledButtons == null) {
			disabledButtons = calculateDisabledButtons();
		}
		return disabledButtons;
	}

	public List<AbcdActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
