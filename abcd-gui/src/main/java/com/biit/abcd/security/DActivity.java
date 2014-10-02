package com.biit.abcd.security;

import com.biit.liferay.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and cannot do. The string for each activity
 * must be unique.
 */
public enum DActivity implements IActivity {
	READ("Read"),

	BUILDING_BLOCK_EDITING("BuildingBlockEditMenuOption"),

	BUILDING_BLOCK_ADD_FROM_FORM("BuildingBlockAdd"),

	FORM_EDITING("FormEditing"),

	FORM_FLOW_EDITING("FormFlowEditing"),

	FORM_STATUS_UPGRADE("FormStatusUpgrade"),

	FORM_STATUS_DOWNGRADE("FormStatusDowngrade"),

	FORM_CHANGE_GROUP("ChangeFormGroup"),

	FORM_CREATE("CreateNewForm"),

	FORM_VERSION("CreateNewFormVersion"),

	ELEMENTS_EDIT("ElementNameEditing"),

	ELEMENTS_ADD_AND_REMOVE("addElements"),

	ELEMENTS_ORDER("orderElements"),

	FORM_DESCRIPTION("CreateNewFormVersion"),

	USER_EDIT_OWN_DATA("UserEditOwnData"),

	ACCESS_TO_ANY_GROUP("AccessToAnyGroup"),

	FORM_DELETE_GROUP("FormDeleteGroup"),

	XFORMS_EXPORT("XFormsExport"),

	XFORMS_PUBLISH("XFormsPublish"),

	XML_VALIDATOR_AGAINST_FORM("XmlValidatorAgainstForm"),

	FORM_ANALYSIS("FormAnalysis"),

	ADMIN_FORMS("AdminForms");

	private String tag;

	DActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}
}
