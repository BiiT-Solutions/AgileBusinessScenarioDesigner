package com.biit.abcd.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	 * Can do administration task for forms. Also has by default all ABCD
	 * manager permissions.
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

	private static final AbcdActivity[] WEB_SERVICES_PERMISSIONS = {

	AbcdActivity.USE_WEB_SERVICE

	};

	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> globalConstantsAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> applicationAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> webServiceUserPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService instance = new AbcdAuthorizationService();

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
		for (AbcdActivity activity : WEB_SERVICES_PERMISSIONS) {
			webServiceUserPermissions.add(activity);
		}

	}

	protected AbcdAuthorizationService() {
		super();
	}

	public static AbcdAuthorizationService getInstance() {
		return instance;
	}

	@Override
	public Set<IActivity> getRoleActivities(Role role) {
		Set<IActivity> activities = new HashSet<IActivity>();
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

	public UserGroup getDefaultGroup(User user) {
		try {
			return AuthenticationService.getInstance().getDefaultGroup(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotConnectedToWebServiceException e) {
			e.printStackTrace();
		} catch (IOException | AuthenticationRequired e) {
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

	public boolean isUserAuthorizedInAnyOrganization(User user, IActivity activity) throws IOException,
			AuthenticationRequired {
		
		// Check isUserAuthorizedActivity (own permissions)
		if (isAuthorizedActivity(user, activity)) {
			return true;
		}
		// Get all organizations of user
		Set<Organization> organizations = getUserOrganizations(user);
		for (Organization organization : organizations) {
			if (isAuthorizedActivity(user, organization, activity)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAuthorizedActivity(User user, Form form, IActivity activity) {
		if (form == null || form.getOrganizationId() == null) {
			return false;
		}
		return isAuthorizedActivity(user, form.getOrganizationId(), activity);
	}

	public boolean isAuthorizedActivity(User user, Long organizationId, IActivity activity) {
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
			Set<Organization> organizations = getUserOrganizations(user);
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

	public Set<Organization> getUserOrganizationsWhereIsAuthorized(User user, IActivity activity) {
		Set<Organization> organizations = new HashSet<>();
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

	protected boolean isAuthorizedToForm(Form form, User user) {
		return isAuthorizedActivity(user, form, AbcdActivity.FORM_EDITING);
	}

	public boolean isAuthorizedToForm(Long formOrganizationId, User user) {
		return isAuthorizedActivity(user, formOrganizationId, AbcdActivity.FORM_EDITING);
	}

}
