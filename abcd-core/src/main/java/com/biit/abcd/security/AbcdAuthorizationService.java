package com.biit.abcd.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.AuthorizationService;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webservice.rest.RestServiceActivity;
import com.liferay.portal.model.Role;

public class AbcdAuthorizationService extends AuthorizationService {

	/**
	 * Can create and edit forms.
	 */
	private static final AbcdActivity[] MANAGE_FORMS_ACTIVITIES = {

	AbcdActivity.FORM_EDITING,

	AbcdActivity.FORM_CREATE,

	AbcdActivity.FORM_CHANGE_GROUP,

	AbcdActivity.FORM_VERSION,

	AbcdActivity.FORM_STATUS_UPGRADE

	};

	/**
	 * Can only read forms.
	 */
	private static final AbcdActivity[] READ_ONLY = {

	AbcdActivity.READ,

	AbcdActivity.USER_EDIT_OWN_DATA

	};

	/**
	 * Can do administration task for forms. Also has by default all ABCD manager permissions.
	 */
	private static final AbcdActivity[] FORM_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	AbcdActivity.FORM_STATUS_DOWNGRADE,

	AbcdActivity.FORM_REMOVE

	};

	private static final AbcdActivity[] MANAGE_GLOBAL_CONSTANTS = {

	AbcdActivity.GLOBAL_VARIABLE_EDITOR

	};

	/**
	 * Manage general application options.
	 */
	private static final AbcdActivity[] APPLICATION_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	AbcdActivity.EVICT_CACHE

	};

	private static final RestServiceActivity[] WEB_SERVICES_PERMISSIONS = {

	RestServiceActivity.USE_WEB_SERVICE

	};

	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> globalConstantsAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> applicationAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> webServiceUserPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService authorizationService = new AbcdAuthorizationService();

	static {
		for (AbcdActivity activity : FORM_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
		for (AbcdActivity activity : MANAGE_FORMS_ACTIVITIES) {
			formManagerPermissions.add(activity);
		}
		for (AbcdActivity activity : READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (AbcdActivity activity : MANAGE_GLOBAL_CONSTANTS) {
			globalConstantsAdministratorPermissions.add(activity);
		}
		for (AbcdActivity activity : APPLICATION_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			applicationAdministratorPermissions.add(activity);
		}
		for (RestServiceActivity activity : WEB_SERVICES_PERMISSIONS) {
			webServiceUserPermissions.add(activity);
		}

	}

	protected AbcdAuthorizationService() {
		super();
	}

	public static AbcdAuthorizationService getInstance() {
		return authorizationService;
	}

	@Override
	public Set<IActivity> getRoleActivities(IRole<Long> role) {
		Set<IActivity> activities = new HashSet<IActivity>();
		AbcdRoles webFormRole = AbcdRoles.parseTag(role.getUniqueName());
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
			activities.addAll(readOnlyPermissions);
			break;
		case APPLICATION_ADMIN:
			activities.addAll(readOnlyPermissions);
			activities.addAll(applicationAdministratorPermissions);
			break;
		case WEB_SERVICE_USER:
			activities.addAll(webServiceUserPermissions);
			break;
		}
		return activities;
	}

	public IGroup<Long> getDefaultGroup(IUser<Long> user) {
		try {
			return AuthenticationService.getInstance().getDefaultGroup(user);
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	public Set<IActivity> getActivitiesOfRoles(List<Role> roles) {
		Set<IActivity> activities = new HashSet<>();
		for (Role role : roles) {
			activities.addAll(getRoleActivities(role));
		}
		return activities;
	}

	public boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity)
			throws UserManagementException {

		// Check isUserAuthorizedActivity (own permissions)
		if (isAuthorizedActivity(user, activity)) {
			return true;
		}
		// Get all organizations of user
		Set<IGroup<Long>> organizations = getUserOrganizations(user);
		for (IGroup<Long> organization : organizations) {
			if (isAuthorizedActivity(user, organization, activity)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAuthorizedActivity(IUser<Long> user, Form form, IActivity activity) {
		if (form == null || form.getOrganizationId() == null) {
			return false;
		}
		return isAuthorizedActivity(user, form.getOrganizationId(), activity);
	}

	public boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity activity) {
		if (organizationId == null) {
			return false;
		}
		IGroup<Long> organization = getOrganization(user, organizationId);
		if (organization == null) {
			return false;
		}
		try {
			return isAuthorizedActivity(user, organization, activity);
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			// For security
			return false;
		}
	}

	private IGroup<Long> getOrganization(IUser<Long> user, Long organizationId) {
		try {
			Set<IGroup<Long>> organizations = getUserOrganizations(user);
			for (IGroup<Long> organization : organizations) {
				if (organization.getId().equals(organizationId)) {
					return organization;
				}
			}
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		return null;
	}

	public Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity) {
		Set<IGroup<Long>> organizations = new HashSet<>();
		try {
			organizations = getUserOrganizations(user);
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				if (!isAuthorizedActivity(user, organization, activity)) {
					itr.remove();
				}
			}
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return organizations;
	}

	protected boolean isAuthorizedToForm(Form form, IUser<Long> user) {
		return isAuthorizedActivity(user, form, AbcdActivity.FORM_EDITING);
	}

	public boolean isAuthorizedToForm(Long formOrganizationId, IUser<Long> user) {
		return isAuthorizedActivity(user, formOrganizationId, AbcdActivity.FORM_EDITING);
	}

}
