package com.biit.abcd.core.security;

import com.biit.usermanager.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and cannot do. The string for each activity
 * must be unique.
 */
public enum AbcdActivity implements IActivity {
	READ("Read"),

	FORM_EDITING("FormEditing"),
	
	FORM_REMOVE("RemoveForm"),
	
	FORM_EXPORT("ExportForm"),

	FORM_CHANGE_GROUP("ChangeFormGroup"),

	FORM_CREATE("CreateNewForm"),

	FORM_VERSION("CreateNewFormVersion"),

	USER_EDIT_OWN_DATA("UserEditOwnData"),

	ADMIN_FORMS("AdminForms"),

	EVICT_CACHE("EvictCache"),

	FORM_STATUS_UPGRADE("FormStatusUpgrade"),
	
	FORM_STATUS_DOWNGRADE("FormStatusDowngrade"), 

	GLOBAL_VARIABLE_EDITOR("GlobalVariables");

	private String tag;

	AbcdActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}
}
