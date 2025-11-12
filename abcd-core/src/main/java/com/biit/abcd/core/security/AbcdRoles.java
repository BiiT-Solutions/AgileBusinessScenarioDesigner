package com.biit.abcd.core.security;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

/**
 * This roles must be inserted in Liferay.
 */
public enum AbcdRoles {

	/* empty role */
	NULL(""),

	READ("abcd_read-only"),

	FORM_EDIT("abcd_manage-forms"),

	FORM_ADMIN("abcd_administration"),

	APPLICATION_ADMIN("abcd_application-administration"),

	GLOBAL_CONSTANTS("abcd_manage-global-constants"),
	
	WEB_SERVICE_USER("abcd_web-service-user");

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
