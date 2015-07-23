package com.biit.abcd.webservice.rest;

import org.springframework.beans.factory.annotation.Autowired;

import com.biit.abcd.security.ISecurityService;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webservice.rest.RestAuthorizationService;
import com.biit.webservice.rest.RestServiceActivity;

public class AbcdRestAuthorizationService extends RestAuthorizationService {

	@Autowired
	private ISecurityService securityService;

	@Override
	public Boolean checkSpecificAuthorization(IUser<Long> user) {
		try {
			return securityService.isUserAuthorizedInAnyOrganization(user, RestServiceActivity.USE_WEB_SERVICE);
		} catch (UserManagementException e) {
			return false;
		}
	}

	@Override
	public IAuthenticationService<Long, Long> getAuthenticationService() {
		return securityService.getAuthenticationService();
	}
}
