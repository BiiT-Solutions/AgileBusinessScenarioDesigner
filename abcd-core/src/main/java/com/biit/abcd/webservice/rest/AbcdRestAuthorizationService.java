package com.biit.abcd.webservice.rest;

import java.io.IOException;
import java.util.StringTokenizer;

import org.glassfish.jersey.internal.util.Base64;

import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.security.AuthenticationService;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.liferay.portal.model.User;

public class AbcdRestAuthorizationService {

	public boolean authorizate(String authCredentials) {

		if (null == authCredentials)
			return false;
		// header value format will be "Basic encodedstring" for Basic
		final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = Base64.decodeAsString(encodedUserPassword);
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		return checkUserPass(username, password);
	}

	private boolean checkUserPass(String user, String password) {
		try {
			User userAuth = AuthenticationService.getInstance().authenticate(user, password);
			return AbcdAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(userAuth,
					AbcdActivity.USE_WEB_SERVICE);
		} catch (InvalidCredentialsException | NotConnectedToWebServiceException | IOException | AuthenticationRequired e) {
			return false;
		}
	}
}
