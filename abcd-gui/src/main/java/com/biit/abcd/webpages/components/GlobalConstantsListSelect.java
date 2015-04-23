package com.biit.abcd.webpages.components;

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
