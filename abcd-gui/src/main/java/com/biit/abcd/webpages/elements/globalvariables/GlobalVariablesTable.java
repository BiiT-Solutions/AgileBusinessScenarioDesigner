package com.biit.abcd.webpages.elements.globalvariables;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class GlobalVariablesTable extends Table {
	private static final long serialVersionUID = 7897917138494222638L;
	
	public enum Properties{
		VARIABLE_NAME
	};

	public GlobalVariablesTable() {
		super();
		setImmediate(true);
		setSelectable(true);
		addContainerProperty(Properties.VARIABLE_NAME, String.class, "", ServerTranslate.tr(LanguageCodes.GLOBAL_VARIABLE_NAME), null, Align.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	public Item addItem(GlobalVariable globalVariable){
		Item item = super.addItem(globalVariable);
		item.getItemProperty(Properties.VARIABLE_NAME).setValue(globalVariable.getName());
		return item;
	}
	
	public Item addItem(Object itemId){
		if(itemId instanceof GlobalVariable){
			return addItem((GlobalVariable)itemId);
		}
		return null;
	}
}
