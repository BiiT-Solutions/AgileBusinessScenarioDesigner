package com.biit.abcd.security;

/**
 * This roles must be inserted in Liferay.
 */
public enum AbcdRoles {

	/* empty role */
	NULL(""),

	READ("abcd_read-only"),

	FORM_EDIT("abcd_manage-forms"),

	FORM_ADMIN("abcd_administration"),

	GLOBAL_CONSTANTS("abcd_manage-global-constants");

	private String stringTag;

	AbcdRoles(String stringTag) {
		this.stringTag = stringTag;
	}

	public String getStringTag() {
		return stringTag;
	}

	public static AbcdRoles parseTag(String stringTag) {
		for (AbcdRoles role : values()) {
			if (stringTag.toLowerCase().equals(role.getStringTag())) {
				return role;
			}
		}
		return NULL;
	}
}
