package com.biit.abcd.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
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
import com.vaadin.server.VaadinServlet;

public class AbcdAuthorizationService extends AuthorizationService {

	private IFormDao formDao;

	/**
	 * Can create and edit forms.
	 */
	private static final DActivity[] WEBFORMS_MANAGE_FORMS_ACTIVITIES = {

	DActivity.READ,

	DActivity.FORM_EDITING,

	DActivity.FORM_FLOW_EDITING,

	DActivity.FORM_STATUS_UPGRADE,

	DActivity.FORM_CREATE,

	DActivity.FORM_CHANGE_GROUP,

	DActivity.FORM_VERSION,

	DActivity.FORM_DESCRIPTION,

	DActivity.ELEMENTS_ADD_AND_REMOVE,

	DActivity.ELEMENTS_ORDER,

	DActivity.ELEMENTS_EDIT,

	DActivity.USER_EDIT_OWN_DATA,

	DActivity.XFORMS_EXPORT,

	DActivity.XFORMS_PUBLISH,

	DActivity.XML_VALIDATOR_AGAINST_FORM,

	DActivity.FORM_ANALYSIS

	};

	/**
	 * Can only read forms.
	 */
	private static final DActivity[] WEBFORMS_READ_ONLY = {

	DActivity.READ,

	DActivity.USER_EDIT_OWN_DATA,

	DActivity.XML_VALIDATOR_AGAINST_FORM,

	DActivity.FORM_ANALYSIS

	};

	/**
	 * Can do administration task for forms. Has by default all Webforms manager permissions.
	 */
	private static final DActivity[] WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	DActivity.ADMIN_FORMS,

	DActivity.FORM_STATUS_DOWNGRADE,

	DActivity.ACCESS_TO_ANY_GROUP,

	DActivity.FORM_DELETE_GROUP,

	DActivity.XML_VALIDATOR_AGAINST_FORM

	};

	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();

	private static AbcdAuthorizationService instance = new AbcdAuthorizationService();

	static {
		for (DActivity activity : WEBFORMS_MANAGE_FORMS_ACTIVITIES) {
			formAdministratorPermissions.add(activity);
			formManagerPermissions.add(activity);
		}
		for (DActivity activity : WEBFORMS_READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (DActivity activity : WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
	}

	private AuthorizationByFormPool authorizationFormPool;

	public AbcdAuthorizationService() {
		super();
		authorizationFormPool = new AuthorizationByFormPool();

		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	public static AbcdAuthorizationService getInstance() {
		return instance;
	}

	/**
	 * User is allowed to do an activity.
	 * 
	 * @param user
	 * @param activity
	 * @return
	 */
	@Override
	public boolean isAuthorizedActivity(User user, IActivity activity) {
		try {
			return super.isAuthorizedActivity(user, activity);
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
			return false;
		}
	}

	/**
	 * User can do an activity on a form if the user has the permissions and pertains to the same group that the form.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean isAuthorizedActivity(Form form, User user, DActivity activity) {
		if (user == null) {
			return false;
		}
		// Is it in the pool?
		Boolean authorized = authorizationFormPool.isAuthorizedActivity(form, user, activity);
		if (authorized != null) {
			return authorized;
		}

		// Calculate authorization.
		authorized = isInFormGroup(form, user) && isAuthorizedActivity(user, activity);
		authorizationFormPool.addUser(form, user, activity, authorized);
		return authorized;
	}

	/**
	 * User can do an activity on a form if the user has the permissions, is not already in use and pertains to the same
	 * group that the form.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean isAuthorizedActivityAndNotInUse(Form form, User user, DActivity activity) {
		return isAuthorizedActivity(form, user, activity) && isNotUsedByOtherUser(form);
	}

	/**
	 * Remove all cached permissions for a specific form. This actions must be done when permissions are changed by some
	 * actions and must be updated immediately (i.e. when a form changes to final design status).
	 * 
	 * @param form
	 */
	public void resetFormCachedPermissions(Form form) {
		authorizationFormPool.removeForm(form);
	}

	/**
	 * A form is editable if user has permissions, the form is not already in use and the form is editable.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public boolean canEditForm(Form form, User user, DActivity activity) {
		return isAuthorizedActivityAndNotInUse(form, user, activity) && isFormEditable(form);
	}

	@Override
	public List<IActivity> getRoleActivities(Role role) {
		List<IActivity> activities = new ArrayList<IActivity>();
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
		}
		return activities;
	}

	/**
	 * Can write to a form if the user pertains to the form group and no other user is editing the form. If the form has
	 * no group defined, any user can access to it.
	 * 
	 * @param form
	 * @param user
	 * @return
	 */
	private boolean isInFormGroup(Form form, User user) {
		if (form == null || form.getUserGroup() == null) {
			return true;
		}
		// Some administrators can access to any form.
		if (isAuthorizedActivity(user, DActivity.ACCESS_TO_ANY_GROUP)) {
			return true;
		}
		try {
			if (!AuthenticationService.getInstance().isInGroup(form.getUserGroup(), user)) {
				return false;
			}
			return true;
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
		} catch (NotConnectedToWebServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * A form is editable if it is the last version.
	 * 
	 * @param form
	 * @return
	 */
	private boolean isFormEditable(Form form) {
		if (form == null) {
			return false;
		}
		// Not stored, then this user has created the form.
		if (form.getId() == null) {
			return true;
		}
		return form.getVersion().equals(formDao.getLastVersion(form));
	}

	public UserGroup getDefaultGroup(User user) {
		try {
			return AuthenticationService.getInstance().getDefaultGroup(user);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotConnectedToWebServiceException e) {
			e.printStackTrace();
		} catch (IOException | AuthenticationRequired e) {
			MessageManager.showError(e.getMessage());
		}
		return null;
	}

	public boolean isNotUsedByOtherUser(Form form) {
		// if (form != null && form.getCurrentUser() != null
		// && form.getCurrentUser().getUserId() != UserSessionHandler.getUser().getUserId()) {
		// return false;
		// }
		return true;
	}

	public boolean isEditable(Form form) {
		return false;
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
