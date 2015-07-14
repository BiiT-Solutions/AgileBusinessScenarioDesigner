package com.biit.abcd.webservice.rest;

import java.io.IOException;

import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.webservice.rest.RestAuthorizationService;
import com.biit.webservice.rest.RestServiceActivity;
import com.liferay.portal.model.User;

public class AbcdRestAuthorizationService extends RestAuthorizationService {

	@Override
	public Boolean checkSpecificAuthorization(User user) {
		try {
			return AbcdAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(user,
					RestServiceActivity.USE_WEB_SERVICE);
		} catch (IOException | AuthenticationRequired e) {
			return false;
		}
	}
}
