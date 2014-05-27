package com.biit.abcd.webpages.elements.decisiontable;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class QuestionValueEditCell extends EditCellComponent{
	private static final long serialVersionUID = -3553182337703247712L;
	
	public QuestionValueEditCell(){
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
