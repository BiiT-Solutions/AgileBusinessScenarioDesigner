package com.biit.abcd.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.abcd.MessageManager;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

@Component
public class AbcdAuthorizationService extends AuthorizationService {

	@Autowired
	private IFormDao formDao;

	/**
	 * Can create and edit building blocks.
	 */
	private static final DActivity[] WEBFORMS_MANAGE_BUILDING_BLOCS_ACTIVITIES = {

	DActivity.READ,

	DActivity.BUILDING_BLOCK_EDITING,

	DActivity.BUILDING_BLOCK_ADD_FROM_FORM,

	DActivity.ELEMENTS_ADD_AND_REMOVE,

	DActivity.ELEMENTS_ORDER,

	DActivity.ELEMENTS_EDIT,

	DActivity.USER_EDIT_OWN_DATA,

	DActivity.XML_VALIDATOR_AGAINST_FORM

	};

	/**
	 * Can create and edit forms.
	 */
	private static final DActivity[] WEBFORMS_MANAGE_FORMS_ACTIVITIES = {

	DActivity.READ,

	DActivity.FORM_EDITING,

	DActivity.FORM_FLOW_EDITING,

	DActivity.FORM_STATUS_UPGRADE,

	DActivity.FORM_CREATE,

	DActivity.FORM_CHANGE_GROUP,

	DActivity.FORM_VERSION,

	DActivity.FORM_DESCRIPTION,

	DActivity.ELEMENTS_ADD_AND_REMOVE,

	DActivity.ELEMENTS_ORDER,

	DActivity.ELEMENTS_EDIT,

	DActivity.USER_EDIT_OWN_DATA,

	DActivity.XFORMS_EXPORT,

	DActivity.XFORMS_PUBLISH,

	DActivity.XML_VALIDATOR_AGAINST_FORM,

	DActivity.FORM_ANALYSIS

	};

	/**
	 * Can only read forms.
	 */
	private static final DActivity[] WEBFORMS_READ_ONLY = {

	DActivity.READ,

	DActivity.USER_EDIT_OWN_DATA,

	DActivity.XML_VALIDATOR_AGAINST_FORM,

	DActivity.FORM_ANALYSIS

	};

	/**
	 * Can do administration task for forms. Has by default all Webforms manager permissions.
	 */
	private static final DActivity[] WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	DActivity.FORM_STATUS_DOWNGRADE,

	DActivity.ACCESS_TO_ANY_GROUP,

	DActivity.FORM_DELETE_GROUP,

	DActivity.XML_VALIDATOR_AGAINST_FORM

	};

	private static List<IActivity> buildingBlockManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService instance = new AbcdAuthorizationService();

	static {
		for (DActivity activity : WEBFORMS_MANAGE_BUILDING_BLOCS_ACTIVITIES) {
			buildingBlockManagerPermissions.add(activity);
		}
		for (DActivity activity : WEBFORMS_MANAGE_FORMS_ACTIVITIES) {
			formManagerPermissions.add(activity);
			formAdministratorPermissions.add(activity);
		}
		for (DActivity activity : WEBFORMS_READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (DActivity activity : WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
	}

	private AuthorizationByFormPool authorizationFormPool;

	public AbcdAuthorizationService() {
		super();
		authorizationFormPool = new AuthorizationByFormPool();
	}

	public static AbcdAuthorizationService getInstance() {
		return instance;
	}

	/**
	 * User is allowed to do an activity.
	 * 
	 * @param user
	 * @param activity
	 * @return
	 */
	@Override
	public boolean isAuthorizedActivity(User user, IActivity activity) {
		try {
			return super.isAuthorizedActivity(user, activity);
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
			return false;
		}
	}

	/**
	 * User can do an activity on a form if the user has the permissions and pertains to the same group that the form.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean isAuthorizedActivity(Form form, User user, DActivity activity) {
		if (user == null) {
			return false;
		}
		// Is it in the pool?
		Boolean authorized = authorizationFormPool.isAuthorizedActivity(form, user, activity);
		if (authorized != null) {
			return authorized;
		}

		// Calculate authorization.
		authorized = isInFormGroup(form, user) && isAuthorizedActivity(user, activity);
		authorizationFormPool.addUser(form, user, activity, authorized);
		return authorized;
	}

	/**
	 * User can do an activity on a form if the user has the permissions, is not already in use and pertains to the same
	 * group that the form.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean isAuthorizedActivityAndNotInUse(Form form, User user, DActivity activity) {
		return isAuthorizedActivity(form, user, activity) && isNotUsedByOtherUser(form);
	}

	/**
	 * Remove all cached permissions for a specific form. This actions must be done when permissions are changed by some
	 * actions and must be updated immediately (i.e. when a form changes to final design status).
	 * 
	 * @param form
	 */
	public void resetFormCachedPermissions(Form form) {
		authorizationFormPool.removeForm(form);
	}

	/**
	 * A form is editable if user has permissions, the form is not already in use and the form is editable.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean canEditForm(Form form, User user, DActivity activity) {
		return isAuthorizedActivityAndNotInUse(form, user, activity) && isFormEditable(form);
	}

	@Override
	public List<IActivity> getRoleActivities(Role role) {
		List<IActivity> activities = new ArrayList<IActivity>();
		if (role.getName().toLowerCase().equals("webforms_manage-building_blocks")) {
			return buildingBlockManagerPermissions;
		} else if (role.getName().toLowerCase().equals("webforms_manage-forms")) {
			return formManagerPermissions;
		} else if (role.getName().toLowerCase().equals("webforms_read-only")) {
			return readOnlyPermissions;
		} else if (role.getName().toLowerCase().equals("webforms_manage-forms_administration")) {
			return formAdministratorPermissions;
		}
		return activities;
	}

	/**
	 * Can write to a form if the user pertains to the form group and no other user is editing the form. If the form has
	 * no group defined, any user can access to it.
	 * 
	 * @param form
	 * @param user
	 * @return
	 */
	private boolean isInFormGroup(Form form, User user) {
		if (form == null || form.getGroup() == null) {
			return true;
		}
		// Some administrators can access to any form.
		if (isAuthorizedActivity(user, DActivity.ACCESS_TO_ANY_GROUP)) {
			return true;
		}
		try {
			if (!AuthenticationService.getInstance().isInGroup(form.getGroup(), user)) {
				return false;
			}
			return true;
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
		} catch (NotConnectedToWebServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * A form is editable if it is the last version.
	 * 
	 * @param form
	 * @return
	 */
	private boolean isFormEditable(Form form) {
		if (form == null) {
			return false;
		}
		return form.getVersion().equals(formDao.getLastVersion(form));
	}

	public UserGroup getDefaultGroup(User user) {
		try {
			return AuthenticationService.getInstance().getDefaultGroup(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotConnectedToWebServiceException e) {
			e.printStackTrace();
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
		}
		return null;
	}

	public boolean isNotUsedByOtherUser(Form form) {
		// if (form != null && form.getCurrentUser() != null
		// && form.getCurrentUser().getUserId() != UserSessionHandler.getUser().getUserId()) {
		// return false;
		// }
		return true;
	}

	public boolean isEditable(Form form) {
		return false;
	}

}
