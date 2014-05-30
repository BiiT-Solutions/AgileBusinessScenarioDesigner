package com.biit.abcd.webpages.elements.decisiontable;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ActionValueEditCell extends EditCellComponent {
	private static final long serialVersionUID = 5033744155212556036L;

	public ActionValueEditCell() {
		super();
		setLabel(" ");
		addRemoveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6253961924451407630L;

			@Override
			public void buttonClick(ClickEvent event) {
				setLabel(" ");
			}
		});
	}

}
