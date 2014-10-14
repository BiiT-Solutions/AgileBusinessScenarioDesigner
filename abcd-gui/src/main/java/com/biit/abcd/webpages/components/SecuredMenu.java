package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.WebMap;
import com.liferay.portal.model.User;
import com.vaadin.ui.Button;

/**
 * Add permissions to enable buttons. A form must be previously selected.
 * 
 */
public abstract class SecuredMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = -5461554340968281793L;
	private static final List<DActivity> activityPermissions = new ArrayList<DActivity>(
			Arrays.asList(DActivity.FORM_EDITING));
	private Set<Button> disabledButtons = null;

	private Set<Button> calculateDisabledButtons() {
		User user = UserSessionHandler.getUser();
		// Check permissions.
		boolean editionEnabled = false;
		boolean inUse = true;
		if (user == null || UserSessionHandler.getFormController() == null
				|| UserSessionHandler.getFormController().getForm() == null) {
			AbcdLogger.info(this.getClass().getName(), "User redirected to login screen.");
			ApplicationFrame.navigateTo(WebMap.getLoginPage());
		} else {
			// Form is not in use.
			if (!AbcdAuthorizationService.getInstance().isFormAlreadyInUse(
					UserSessionHandler.getFormController().getForm().getId(), user)) {
				inUse = false;
				// user has permissions to edit this form.
				if (!AbcdAuthorizationService.getInstance().isFormReadOnly(
						UserSessionHandler.getFormController().getForm(), user)) {
					editionEnabled = true;
				}
			}
		}

		Set<Button> disabledButtons = new HashSet<>();
		// Disable all Buttons
		if (!editionEnabled) {
			if (inUse) {
				MessageManager.showWarning(LanguageCodes.WARNING_FORM_IN_USE,
						LanguageCodes.WARNING_FORM_IN_USE_DESCRIPTION);
			} else {
				MessageManager.showWarning(LanguageCodes.WARNING_FORM_READ_ONLY,
						LanguageCodes.WARNING_FORM_READ_ONLY_DESCRIPTION);
			}
			for (Button button : getButtons()) {
				disabledButtons.add(button);
			}
		}
		return disabledButtons;
	}

	/**
	 * Return a list of buttons that must be disabled due to user permissions limitations.
	 * 
	 * @return
	 */
	public Set<Button> getDisabledButtons() {
		if (disabledButtons == null) {
			disabledButtons = calculateDisabledButtons();
		}
		return disabledButtons;
	}

	public List<DActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
