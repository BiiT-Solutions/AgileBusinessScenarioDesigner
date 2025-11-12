package com.biit.abcd.core.security;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IActivityManager;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.liferay.portal.log.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class SecurityService implements ISecurityService {

    @Autowired
    private IAuthenticationService<Long, Long> authenticationService;

    @Autowired
    private IAuthorizationService<Long, Long, Long> authorizationService;

    @Autowired
    private IActivityManager<Long, Long, Long> activityManager;

    protected SecurityService() {
        super();
    }

    public IGroup<Long> getDefaultGroup(IUser<Long> user) {
        try {
            return getAuthenticationService().getDefaultGroup(user);
        } catch (UserManagementException | UserDoesNotExistException | InvalidCredentialsException e) {
            SecurityLogger.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public Set<IActivity> getActivitiesOfRoles(List<IRole<Long>> roles) {
        Set<IActivity> activities = new HashSet<>();
        for (IRole<Long> role : roles) {
            try {
                activities.addAll(getActivityManager().getRoleActivities(role));
            } catch (InvalidCredentialsException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }
        }
        SecurityLogger.debug(this.getClass().getName(), "Activities for roles '" + roles + "' are '" + activities + "'.");
        return activities;
    }

    @Override
    public boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity)
            throws UserManagementException {
        // Check isUserAuthorizedActivity (own permissions)
        try {
            if (getActivityManager().isAuthorizedActivity(user, activity)) {
                SecurityLogger.info(this.getClass().getName(), "User '" + user + "' is authorized for '" + activity + "'.");
                return true;
            }
        } catch (UserDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }

        // Get all organizations of user
        Set<IGroup<Long>> organizations = getUserOrganizations(user);
        for (IGroup<Long> organization : organizations) {
            if (isAuthorizedActivity(user, organization, activity)) {
                SecurityLogger.info(this.getClass().getName(),
                        "User '" + user + "' has authorization for '" + activity + "' at '" + organization + "'.");
                return true;
            }
        }
        SecurityLogger.info(this.getClass().getName(), "User '" + user + "' is NOT authorized for '" + activity + "'.");
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
            return getActivityManager().isAuthorizedActivity(user, organization, activity);
        } catch (UserManagementException | UserDoesNotExistException | OrganizationDoesNotExistException |
                 InvalidCredentialsException e) {
            SecurityLogger.errorMessage(this.getClass().getName(), e);
            // For security
            return false;
        }
    }

    private IGroup<Long> getOrganization(IUser<Long> user, Long organizationId) {
        try {
            Set<IGroup<Long>> organizations = getUserOrganizations(user);
            for (IGroup<Long> organization : organizations) {
                if (organization.getUniqueId().equals(organizationId)) {
                    return organization;
                }
            }
        } catch (UserManagementException e) {
            SecurityLogger.errorMessage(this.getClass().getName(), e);
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
                if (!getActivityManager().isAuthorizedActivity(user, organization, activity)) {
                    itr.remove();
                }
            }
        } catch (UserManagementException | UserDoesNotExistException | OrganizationDoesNotExistException |
                 InvalidCredentialsException e) {
            SecurityLogger.errorMessage(this.getClass().getName(), e);
        }
        SecurityLogger.info(this.getClass().getName(),
                "User '" + user + "' is authorized for '" + activity + "' at '" + organizations + "'.");
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
        try {
            return getAuthorizationService().getUserOrganizations(user);
        } catch (UserDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
        try {
            return getActivityManager().isAuthorizedActivity(user, activity);
        } catch (UserDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }

    @Override
    public void reset() {
        getAuthorizationService().reset();
    }

    @Override
    public IGroup<Long> getOrganization(long organizationId) throws UserManagementException {
        try {
            return getAuthorizationService().getOrganization(organizationId);
        } catch (OrganizationDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity) throws UserManagementException {
        try {
            return getActivityManager().isAuthorizedActivity(user, organization, activity);
        } catch (UserDoesNotExistException | OrganizationDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }

    @Override
    public IUser<Long> getUserByEmail(String userEmail) throws UserManagementException, UserDoesNotExistException {
        try {
            return getAuthenticationService().getUserByEmail(userEmail);
        } catch (InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }

    @Override
    public IAuthorizationService<Long, Long, Long> getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthenticationService(IAuthenticationService<Long, Long> authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public IAuthenticationService<Long, Long> getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthorizationService(IAuthorizationService<Long, Long, Long> authorizationService) {
        this.authorizationService = authorizationService;
    }

    public IActivityManager<Long, Long, Long> getActivityManager() {
        return activityManager;
    }

    public void setActivityManager(IActivityManager<Long, Long, Long> activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public IUser<Long> getUserById(Long userId) throws UserManagementException {
        if (userId == null) {
            return null;
        }
        try {
            return authenticationService.getUserById(userId);
        } catch (UserDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error when connecting to the User Manager System!", e);
        }
    }
}
