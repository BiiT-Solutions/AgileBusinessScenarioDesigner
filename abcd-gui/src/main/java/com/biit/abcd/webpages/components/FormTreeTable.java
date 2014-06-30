package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;

public class FormTreeTable extends TreeObjectTable {
	private static final long serialVersionUID = 6016194810449244086L;
	
	protected enum FormTreeTableProperties {
		ELEMENT_NAME, RULES
	};

	public FormTreeTable() {
		addContainerProperty(FormTreeTableProperties.RULES, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_RULES), null, Align.CENTER);
	}
}
