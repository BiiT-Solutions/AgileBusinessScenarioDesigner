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

	GLOBAL_VARIABLE_EDITOR("GlobalVariables"),

	PUBLISH_TO_KNOWLEDGE_MANAGER("PublishToKnowledgeManager");

	private String tag;

	AbcdActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}
}
