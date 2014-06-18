package com.biit.abcd.webpages.elements.globalvariables;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.utils.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class VariableDataTable extends Table {
	private static final long serialVersionUID = -5563887412506415508L;

	public enum Properties {
		VARIABLE_VALUE, VARIABLE_VALID_FROM, VARIABLE_VALID_TO
	};

	public VariableDataTable() {
		super();
		setImmediate(true);
		setSelectable(true);
		addContainerProperty(Properties.VARIABLE_VALUE, String.class, "",
				ServerTranslate.tr(LanguageCodes.GLOBAL_VARIABLE_VALUE), null, Align.CENTER);
		addContainerProperty(Properties.VARIABLE_VALID_FROM, String.class, "",
				ServerTranslate.tr(LanguageCodes.GLOBAL_VARIABLE_VALID_FROM), null, Align.CENTER);
		addContainerProperty(Properties.VARIABLE_VALID_TO, String.class, "",
				ServerTranslate.tr(LanguageCodes.GLOBAL_VARIABLE_VALID_TO), null, Align.CENTER);
	}

	public void setVariable(GlobalVariable variable) {
		if (variable != null) {
			for (VariableData data : variable.getData()) {
				addItem(data);
			}
		}else{
			removeAllItems();
		}
	}

	@SuppressWarnings("unchecked")
	public Item addItem(VariableData variableData) {
		Item item = super.addItem(variableData);
		item.getItemProperty(Properties.VARIABLE_VALUE).setValue(variableData.getValue());
		item.getItemProperty(Properties.VARIABLE_VALID_FROM).setValue(
				DateManager.convertDateToString(variableData.getValidFrom()));
		item.getItemProperty(Properties.VARIABLE_VALID_TO).setValue(
				DateManager.convertDateToString(variableData.getValidTo()));
		return item;
	}

	@Override
	public Item addItem(Object itemId) {
		if (itemId instanceof VariableData) {
			return addItem((VariableData) itemId);
		}
		return null;
	}
}
