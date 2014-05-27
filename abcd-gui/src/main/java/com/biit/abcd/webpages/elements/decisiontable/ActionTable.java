package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Table;

public class ActionTable extends Table {

	private static final long serialVersionUID = -8737505874064899775L;

	enum Columns {
		ACTION
	};

	public ActionTable() {
		setImmediate(true);
		setSizeFull();
		addContainerProperty(Columns.ACTION, String.class, "",
				ServerTranslate.tr(LanguageCodes.ACTION_TABLE_HEADER_ACTION), null, Align.CENTER);

	}
}
