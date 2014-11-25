package com.biit.abcd.webpages.components;

import com.vaadin.ui.ComboBox;

public class ComparableComboBox extends ComboBox implements Comparable<ComboBox> {
	private static final long serialVersionUID = -8918696467137357949L;

	@Override
	public int compareTo(ComboBox textField) {
		return getValue().toString().compareTo(textField.getValue().toString());
	}

}
