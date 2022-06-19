package com.biit.abcd.core.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IRoleActivities;
import com.biit.webservice.rest.RestServiceActivity;

@Repository
public class RoleActivities implements IRoleActivities {

	/**
	 * Can create and edit forms.
	 */
	private static final AbcdActivity[] MANAGE_FORMS_ACTIVITIES = {

	AbcdActivity.FORM_EDITING,

	AbcdActivity.FORM_CREATE,

	AbcdActivity.FORM_CHANGE_GROUP,

	AbcdActivity.FORM_VERSION,

	AbcdActivity.FORM_STATUS_UPGRADE,	
	
	AbcdActivity.FORM_EXPORT,

	AbcdActivity.PUBLISH_TO_KNOWLEDGE_MANAGER

	};

	/**
	 * Can only read forms.
	 */
	private static final AbcdActivity[] READ_ONLY = {

	AbcdActivity.READ,

	AbcdActivity.USER_EDIT_OWN_DATA

	};

	/**
	 * Can do administration task for forms. Also has by default all ABCD
	 * manager permissions.
	 */
	private static final AbcdActivity[] FORM_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	AbcdActivity.FORM_STATUS_DOWNGRADE,

	AbcdActivity.FORM_REMOVE,

	};

	private static final AbcdActivity[] MANAGE_GLOBAL_CONSTANTS = {

	AbcdActivity.GLOBAL_VARIABLE_EDITOR

	};

	/**
	 * Manage general application options.
	 */
	private static final AbcdActivity[] APPLICATION_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	AbcdActivity.EVICT_CACHE

	};

	private static final RestServiceActivity[] WEB_SERVICES_PERMISSIONS = {

	RestServiceActivity.USE_WEB_SERVICE

	};

	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> globalConstantsAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> applicationAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> webServiceUserPermissions = new ArrayList<IActivity>();

	static {
		for (AbcdActivity activity : FORM_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
		for (AbcdActivity activity : MANAGE_FORMS_ACTIVITIES) {
			formManagerPermissions.add(activity);
		}
		for (AbcdActivity activity : READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (AbcdActivity activity : MANAGE_GLOBAL_CONSTANTS) {
			globalConstantsAdministratorPermissions.add(activity);
		}
		for (AbcdActivity activity : APPLICATION_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			applicationAdministratorPermissions.add(activity);
		}
		for (RestServiceActivity activity : WEB_SERVICES_PERMISSIONS) {
			webServiceUserPermissions.add(activity);
		}

	}

	@Override
	public Set<IActivity> getRoleActivities(IRole<Long> role) {
		Set<IActivity> activities = new HashSet<IActivity>();
		AbcdRoles abcdRole = AbcdRoles.parseTag(role.getUniqueName());
		switch (abcdRole) {
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
		case GLOBAL_CONSTANTS:
			activities.addAll(globalConstantsAdministratorPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case APPLICATION_ADMIN:
			activities.addAll(applicationAdministratorPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case WEB_SERVICE_USER:
			activities.addAll(webServiceUserPermissions);
			break;
		}
		return activities;
	}

}
