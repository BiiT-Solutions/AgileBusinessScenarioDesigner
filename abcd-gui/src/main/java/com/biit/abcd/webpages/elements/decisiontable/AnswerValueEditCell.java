package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.webpages.components.SecuredEditCellComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AnswerValueEditCell extends SecuredEditCellComponent {
	private static final long serialVersionUID = 5684048033655720281L;

	public AnswerValueEditCell() {
		super();
		setLabel(" ");
		setCellBehaviour();
	}

	private void setCellBehaviour() {
		addRemoveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6253961924451407630L;

			@Override
			public void buttonClick(ClickEvent event) {
				setLabel(" ");
			}
		});
	}
}