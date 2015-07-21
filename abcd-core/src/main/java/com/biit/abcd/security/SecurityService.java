package com.biit.abcd.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.liferay.portal.model.Role;

@Component
public class SecurityService implements ISecurityService {

	@Autowired
	private IAuthenticationService<Long, Long> authenticationService;

	@Autowired
	private IAuthorizationService<Long, Long, Long> authorizationService;

	protected SecurityService() {
		super();
	}

	public IGroup<Long> getDefaultGroup(IUser<Long> user) {
		try {
			return getAuthenticationService().getDefaultGroup(user);
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	public Set<IActivity> getActivitiesOfRoles(List<Role> roles) {
		Set<IActivity> activities = new HashSet<>();
		for (Role role : roles) {
			activities.addAll(getAuthorizationService().getRoleActivities(role));
		}
		return activities;
	}

	@Override
	public boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity)
			throws UserManagementException {

		// Check isUserAuthorizedActivity (own permissions)
		if (getAuthorizationService().isAuthorizedActivity(user, activity)) {
			return true;
		}
		// Get all organizations of user
		Set<IGroup<Long>> organizations = getUserOrganizations(user);
		for (IGroup<Long> organization : organizations) {
			if (getAuthorizationService().isAuthorizedActivity(user, organization, activity)) {
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

	@Override
	public boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity activity) {
		if (organizationId == null) {
			return false;
		}
		IGroup<Long> organization = getOrganization(user, organizationId);
		if (organization == null) {
			return false;
		}
		try {
			return getAuthorizationService().isAuthorizedActivity(user, organization, activity);
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

	@Override
	public Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity) {
		Set<IGroup<Long>> organizations = new HashSet<>();
		try {
			organizations = getUserOrganizations(user);
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				if (!getAuthorizationService().isAuthorizedActivity(user, organization, activity)) {
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

	@Override
	public boolean isAuthorizedToForm(Long formOrganizationId, IUser<Long> user) {
		return isAuthorizedActivity(user, formOrganizationId, AbcdActivity.FORM_EDITING);
	}

	@Override
	public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException {
		return getAuthorizationService().getUserOrganizations(user);
	}

	@Override
	public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
		return getAuthorizationService().isAuthorizedActivity(user, activity);
	}

	@Override
	public void reset() {
		getAuthorizationService().reset();
	}

	@Override
	public IGroup<Long> getOrganization(long organizationId) throws UserManagementException {
		return getAuthorizationService().getOrganization(organizationId);
	}

	@Override
	public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity)
			throws UserManagementException {
		return getAuthorizationService().isAuthorizedActivity(user, organization, activity);
	}

	@Override
	public IUser<Long> getUserByEmail(String userEmail) throws UserManagementException {
		return getAuthenticationService().getUserByEmail(userEmail);
	}

	public IAuthorizationService<Long, Long, Long> getAuthorizationService() {
		return authorizationService;
	}

	public void setAuthenticationService(IAuthenticationService<Long, Long> authenticationService) {
		this.authenticationService = authenticationService;
	}

	public IAuthenticationService<Long, Long> getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthorizationService(IAuthorizationService<Long, Long, Long> authorizationService) {
		this.authorizationService = authorizationService;
	}
}
