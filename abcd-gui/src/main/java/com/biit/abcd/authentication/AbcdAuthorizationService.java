package com.biit.abcd.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.UiAccesser;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

public class AbcdAuthorizationService extends AuthorizationService {

	/**
	 * Read
	 */
	private static final AbcdActivity[] READ_ONLY = {

	AbcdActivity.READ

	};

	private static final AbcdActivity[] FORM_EDIT = {

	AbcdActivity.EDIT_FORM

	};

	private static final AbcdActivity[] WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	AbcdActivity.ADMIN_FORMS

	};

	private static List<IActivity> buildingBlockManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService instance = new AbcdAuthorizationService();

	static {
		for (IActivity activity : READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (IActivity activity : FORM_EDIT) {
			formManagerPermissions.add(activity);
			formAdministratorPermissions.add(activity);
		}
		for (IActivity activity : WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
	}

	public static AbcdAuthorizationService getInstance() {
		return instance;
	}

	private UserGroupService userGroupService = new UserGroupService();

	public AbcdAuthorizationService() {
		super();
		userGroupService.serverConnection();
	}

	/**
	 * Get activities associated to a role that has been assigned to the user.
	 */
	@Override
	public List<IActivity> getRoleActivities(Role role) {
		List<IActivity> activities = new ArrayList<IActivity>();
		AbcdRoles webFormRole = AbcdRoles.parseTag(role.getName());
		switch (webFormRole) {
		case FORM_ADMIN:
			activities.addAll(formAdministratorPermissions);
			activities.addAll(buildingBlockManagerPermissions);
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
		}
		return activities;
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

	public boolean isAuthorizedActivity(User user, Form form, IActivity activity) {
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

	public Organization getOrganization(User user, Long organizationId) {
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

	public boolean isAuthorizedToForm(Form form, User user) {
		// TODO
		return true;
	}

	public boolean isAuthorizedToForm(Long formId, User user) {
		// TODO
		return true;
	}

	public boolean isFormEditable(Form form, User user) {
		// TODO
		return true;
	}

	public boolean isFormReadOnly(Form form, User user) {
		boolean formIsInUse = UiAccesser.getUserUsingForm(form) != null;
		return (!formIsInUse && !isAuthorizedToForm(form, user))
				|| (formIsInUse && UiAccesser.getUserUsingForm(form) != user);
	}

	public boolean isFormReadOnly(Long formId, User user) {
		boolean formIsInUse = UiAccesser.getUserUsingForm(formId) != null;
		return (!formIsInUse && !isAuthorizedToForm(formId, user))
				|| (formIsInUse && UiAccesser.getUserUsingForm(formId) != user);
	}

}
