package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ActionValueEditCell extends EditCellComponent {
	private static final long serialVersionUID = 5033744155212556036L;

	public ActionValueEditCell() {
		super();
		setOnlyEdit(true);
		setCellBehaviour();
	}

	public void setLabel(ExpressionChain action) {
		if ((action == null) || (action.toString().length() == 0)) {
			setLabel("<div style=\"background-color: rgb(179, 46, 46); color: rgb(255,255,255); display: inline;\">"
					+ ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE) + "</div>");
		} else {
			setLabel(action.toString());
		}
	}

	private void setCellBehaviour() {
		addRemoveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6253961924451407630L;

			@Override
			public void buttonClick(ClickEvent event) {
				setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
			}
		});
	}
}
