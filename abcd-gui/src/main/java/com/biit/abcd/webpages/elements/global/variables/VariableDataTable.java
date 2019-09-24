package com.biit.abcd.webpages.elements.global.variables;

import java.util.Date;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.drools.global.variables.interfaces.IVariableData;
import com.biit.utils.date.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class VariableDataTable extends Table {
	private static final long serialVersionUID = -5563887412506415508L;

	public enum Properties {
		VARIABLE_VALUE, VARIABLE_VALID_FROM, VARIABLE_VALID_TO
	};

	public VariableDataTable(GlobalVariable globalVariable) {
		super();
		setImmediate(true);
		setSelectable(true);
		setImmediate(true);
		setNullSelectionAllowed(false);

		if (globalVariable == null) {
			addContainerProperty(Properties.VARIABLE_VALUE, String.class, "",
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE), null, Align.CENTER);
		} else if (globalVariable.getFormat().equals(AnswerFormat.NUMBER)) {
			addContainerProperty(Properties.VARIABLE_VALUE, Double.class, null,
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE), null, Align.CENTER);
		} else if (globalVariable.getFormat().equals(AnswerFormat.DATE)) {
			addContainerProperty(Properties.VARIABLE_VALUE, String.class, "",
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE), null, Align.CENTER);
		} else {
			addContainerProperty(Properties.VARIABLE_VALUE, String.class, "",
					ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALUE), null, Align.CENTER);
		}
		addContainerProperty(Properties.VARIABLE_VALID_FROM, String.class, "",
				ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALID_FROM), null, Align.CENTER);
		addContainerProperty(Properties.VARIABLE_VALID_TO, String.class, "",
				ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_VALID_TO), null, Align.CENTER);
		this.setLocale(UserSessionHandler.getUser().getLocale());

		setColumnExpandRatio(Properties.VARIABLE_VALUE, 1);
		setColumnExpandRatio(Properties.VARIABLE_VALID_FROM, 1);
		setColumnExpandRatio(Properties.VARIABLE_VALID_TO, 1);
	}

	public void setVariable(GlobalVariable variable) {
		if (variable != null) {
			for (IVariableData data : variable.getVariableData()) {
				addRow(data);
			}
		} else {
			removeAllItems();
		}
	}

	public Item addRow(IVariableData variableData) {
		Item item = addItem(variableData);
		refreshItem(variableData, item);
		setValue(variableData);
		return item;
	}

	public VariableData getSelectedVariableData() {
		if (getValue() != null) {
			return (VariableData) getValue();
		}
		return null;
	}

	public void updateItem(VariableData variableData) {
		Item item = getItem(variableData);
		refreshItem(variableData, item);
	}

	@SuppressWarnings("unchecked")
	private void refreshItem(IVariableData variableData, Item item) {
		if (item != null) {
			if (variableData instanceof VariableDataDate) {
				item.getItemProperty(Properties.VARIABLE_VALUE).setValue(DateManager
						.convertDateToString((Date) variableData.getValue(), DateManager.DATE_FORMAT_SIMPLE));
			} else {
				if (variableData.getValue() != null) {
					item.getItemProperty(Properties.VARIABLE_VALUE).setValue(variableData.getValue().toString());
				}
			}
			item.getItemProperty(Properties.VARIABLE_VALID_FROM).setValue(
					DateManager.convertDateToString(variableData.getValidFrom(), DateManager.DATE_FORMAT_SIMPLE));
			if (variableData.getValidTo() == null) {
				// Value set to infinite (null)
				item.getItemProperty(Properties.VARIABLE_VALID_TO).setValue(null);
			} else {
				item.getItemProperty(Properties.VARIABLE_VALID_TO).setValue(
						DateManager.convertDateToString(variableData.getValidTo(), DateManager.DATE_FORMAT_SIMPLE));
			}
		}
	}
}
