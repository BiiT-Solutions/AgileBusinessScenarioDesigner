package com.biit.abcd.authentication;

import com.biit.liferay.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and cannot do. The string for each activity
 * must be unique.
 */
public enum AbcdActivity implements IActivity {

	READ("Read"),

	EDIT_FORM("EditForm"),
	
	ADMIN_FORMS("AdminForms");

	private String tag;

	AbcdActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}
