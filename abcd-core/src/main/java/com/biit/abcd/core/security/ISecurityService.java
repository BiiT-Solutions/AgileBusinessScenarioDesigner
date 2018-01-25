package com.biit.abcd.core.security;

import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface ISecurityService {

	boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity)
			throws UserManagementException;

	IGroup<Long> getOrganization(long organizationId) throws UserManagementException;

	void reset();

	boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException;

	Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException;

	boolean isAuthorizedToForm(Long formOrganizationId, IUser<Long> user);

	IUser<Long> getUserByEmail(String userEmail) throws UserManagementException, UserDoesNotExistException;

	IUser<Long> getUserById(Long userId) throws UserManagementException;

	boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity useWebService) throws UserManagementException;

	boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity formStatusDowngrade);

	Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity);

	IAuthorizationService<Long, Long, Long> getAuthorizationService();

	IAuthenticationService<Long, Long> getAuthenticationService();

}
