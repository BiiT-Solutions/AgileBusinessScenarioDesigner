package com.biit.abcd.webpages.components;

import com.vaadin.ui.TextField;

public class ComparableTextField extends TextField implements Comparable<TextField>{
	private static final long serialVersionUID = -8918696467137357949L;

	@Override
	public int compareTo(TextField textField) {
		return getValue().compareTo(textField.getValue());
	}

}
