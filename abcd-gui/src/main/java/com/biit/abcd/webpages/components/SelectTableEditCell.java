package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SelectTableEditCell extends SecuredEditCellComponent {
	private static final long serialVersionUID = 5033744155212556036L;

	public SelectTableEditCell() {
		super();
		addRemoveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6253961924451407630L;

			@Override
			public void buttonClick(ClickEvent event) {
				setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
			}
		});
	}

	public void setLabel(Object object) {
		if (object == null) {
			setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
		} else {
			if (object instanceof INameAttribute) {
				setLabel(((INameAttribute) object).getName());
			}
		}
	}
}