package com.biit.abcd.webservice.rest;

import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webservice.rest.RestAuthorizationService;
import com.biit.webservice.rest.RestServiceActivity;

public class AbcdRestAuthorizationService extends RestAuthorizationService {

	@Override
	public Boolean checkSpecificAuthorization(IUser<Long> user) {
		try {
			return AbcdAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(user,
					RestServiceActivity.USE_WEB_SERVICE);
		} catch (UserManagementException e) {
			return false;
		}
	}
}
