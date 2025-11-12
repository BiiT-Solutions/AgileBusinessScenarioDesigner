package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.ui.ListSelect;

/**
 * ListSelect initialized with all GlobalConstants defined.
 */
public class GlobalConstantsListSelect extends ListSelect {
	private static final long serialVersionUID = 428056700545087967L;

	public GlobalConstantsListSelect() {
		setSizeFull();
		setNullSelectionAllowed(false);
		setImmediate(true);

		initializeVariableSelectionValues(UserSessionHandler.getGlobalVariablesController().getGlobalVariables());
	}

	private void initializeVariableSelectionValues(List<GlobalVariable> globalVariables) {
		setValue(null);
		removeAllItems();
		if (globalVariables != null) {
			for (GlobalVariable globalVariable : globalVariables) {
				addItem(globalVariable);
				setItemCaption(globalVariable, globalVariable.getName());
			}
			if (!globalVariables.isEmpty()) {
				setValue(globalVariables.get(0));
			}
		}
	}

	@Override
	public GlobalVariable getValue() {
		if (super.getValue() == null) {
			return null;
		}
		return (GlobalVariable) super.getValue();
	}

	@Override
	public void setValue(Object globalVariable) {
		if (globalVariable instanceof GlobalVariable) {
			super.setValue(globalVariable);
		}
	}

}
