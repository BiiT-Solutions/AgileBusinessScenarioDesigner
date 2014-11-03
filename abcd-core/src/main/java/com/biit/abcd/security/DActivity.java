package com.biit.abcd.security;

import com.biit.liferay.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and cannot do. The string for each activity
 * must be unique.
 */
public enum DActivity implements IActivity {
	READ("Read"),

	FORM_EDITING("FormEditing"),

	FORM_CHANGE_GROUP("ChangeFormGroup"),

	FORM_CREATE("CreateNewForm"),

	FORM_VERSION("CreateNewFormVersion"),

	USER_EDIT_OWN_DATA("UserEditOwnData"),

	ADMIN_FORMS("AdminForms"),
	
	EVICT_CACHE("EvictCache"),

	GLOBAL_VARIABLE_EDITOR("GlobalVariables");

	private String tag;

	DActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}
}
