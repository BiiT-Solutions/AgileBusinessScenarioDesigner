package com.biit.abcd.liferay;

import java.io.IOException;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;

public class LiferayServiceAccess {
	private static LiferayServiceAccess instance = new LiferayServiceAccess();

	private UserService userService;

	public static LiferayServiceAccess getInstance() {
		return instance;
	}

	private UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
			userService.serverConnection();
		}
		return userService;
	}

	public IUser<Long> getUserById(Long userId) throws UserDoesNotExistException {
		try {
			return getUserService().getUserById(userId);
		} catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired | WebServiceAccessError e) {
			AbcdLogger.errorMessage(LiferayServiceAccess.class.getName(), e);
		}
		return null;
	}
}
