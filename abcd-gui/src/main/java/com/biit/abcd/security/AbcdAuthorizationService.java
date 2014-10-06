package com.biit.abcd.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class AbcdAuthorizationService extends AuthorizationService {

	/**
	 * Can create and edit forms.
	 */
	private static final DActivity[] MANAGE_FORMS_ACTIVITIES = {

	DActivity.FORM_EDITING,

	DActivity.FORM_CREATE,

	DActivity.FORM_CHANGE_GROUP,

	DActivity.FORM_VERSION

	};

	/**
	 * Can only read forms.
	 */
	private static final DActivity[] READ_ONLY = {

	DActivity.READ,

	DActivity.USER_EDIT_OWN_DATA

	};

	/**
	 * Can do administration task for forms. Has by default all Webforms manager permissions.
	 */
	private static final DActivity[] ADMINISTRATOR_EXTRA_PERMISSIONS = {

	DActivity.ADMIN_FORMS

	};

	private static final DActivity[] MANAGE_GLOBAL_CONSTANTS = {

	DActivity.GLOBAL_VARIABLE_EDITOR

	};

	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> globalConstantsAdministratorPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService instance = new AbcdAuthorizationService();

	static {
		for (DActivity activity : ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
		for (DActivity activity : MANAGE_FORMS_ACTIVITIES) {
			formAdministratorPermissions.add(activity);
			formManagerPermissions.add(activity);
		}
		for (DActivity activity : READ_ONLY) {
			readOnlyPermissions.add(activity);
			formAdministratorPermissions.add(activity);
			formManagerPermissions.add(activity);
		}
		for (DActivity activity : MANAGE_GLOBAL_CONSTANTS) {
			globalConstantsAdministratorPermissions.add(activity);
		}
	}

	public AbcdAuthorizationService() {
		super();
	}

	public static AbcdAuthorizationService getInstance() {
		return instance;
	}

	@Override
	public List<IActivity> getRoleActivities(Role role) {
		List<IActivity> activities = new ArrayList<IActivity>();
		AbcdRoles webFormRole = AbcdRoles.parseTag(role.getName());
		switch (webFormRole) {
		case FORM_ADMIN:
			activities.addAll(formAdministratorPermissions);
			activities.addAll(formManagerPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case FORM_EDIT:
			activities.addAll(formManagerPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case READ:
			activities.addAll(readOnlyPermissions);
			break;
		case NULL:
			break;
		case GLOBAL_CONSTANTS:
			activities.addAll(globalConstantsAdministratorPermissions);
			break;
		}
		return activities;
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

	public boolean isEditable(Form form) {
		return false;
	}

	public Set<IActivity> getActivitiesOfRoles(List<Role> roles) {
		Set<IActivity> activities = new HashSet<>();
		for (Role role : roles) {
			activities.addAll(getRoleActivities(role));
		}
		return activities;
	}

	public boolean isUserAuthorizedInAnyOrganization(User user, IActivity activity) throws IOException,
			AuthenticationRequired {
		// Check isUserAuthorizedActivity (own permissions)
		if (isAuthorizedActivity(user, activity)) {
			return true;
		}

		// Get all organizations of user
		List<Organization> organizations = getUserOrganizations(user);
		for (Organization organization : organizations) {
			if (isAuthorizedActivity(user, organization, activity)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAuthorizedActivity(User user, Form form, IActivity activity) {
		if (form == null || form.getOrganizationId() == null) {
			return false;
		}
		Organization organization = getOrganization(user, form.getOrganizationId());
		if (organization == null) {
			return false;
		}
		try {
			return isAuthorizedActivity(user, organization, activity);
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			// For security
			return false;
		}
	}

	private boolean isAuthorizedActivity(User user, Long organizationId, IActivity activity) {
		if (organizationId == null) {
			return false;
		}
		Organization organization = getOrganization(user, organizationId);
		if (organization == null) {
			return false;
		}
		try {
			return isAuthorizedActivity(user, organization, activity);
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			// For security
			return false;
		}
	}

	private Organization getOrganization(User user, Long organizationId) {
		try {
			List<Organization> organizations = getUserOrganizations(user);
			for (Organization organization : organizations) {
				if (organization.getOrganizationId() == organizationId) {
					return organization;
				}
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		return null;
	}

	public List<Organization> getUserOrganizationsWhereIsAuthorized(User user, IActivity activity) {
		List<Organization> organizations = new ArrayList<>();
		try {
			organizations = getUserOrganizations(user);
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				if (!isAuthorizedActivity(user, organization, activity)) {
					itr.remove();
				}
			}
		} catch (IOException | AuthenticationRequired e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return organizations;
	}

	private boolean isAuthorizedToForm(Form form, User user) {
		return isAuthorizedActivity(user, form, DActivity.FORM_EDITING);
	}

	public boolean isAuthorizedToForm(Long formOrganizationId, User user) {
		return isAuthorizedActivity(user, formOrganizationId, DActivity.FORM_EDITING);
	}

	public boolean isFormReadOnly(Form form, User user) {
		return !isAuthorizedToForm(form, user) || isFormAlreadyInUse(form.getId(), user);
	}

	public boolean isFormReadOnly(Long formId, Long formOrganizationId, User user) {
		return !isAuthorizedToForm(formOrganizationId, user) || isFormAlreadyInUse(formId, user);
	}

	public boolean isFormAlreadyInUse(Long formId, User user) {
		User userUsingForm = UiAccesser.getUserUsingForm(formId);
		return (userUsingForm != null) && userUsingForm != user;
	}

}
