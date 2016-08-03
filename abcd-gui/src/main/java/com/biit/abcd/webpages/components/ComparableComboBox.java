package com.biit.abcd.webpages.components;

import com.vaadin.ui.ComboBox;

public class ComparableComboBox extends ComboBox implements Comparable<ComboBox> {
	private static final long serialVersionUID = -8918696467137357949L;

	@Override
	public int compareTo(ComboBox textField) {
		return getValue() == null ? (textField.getValue() == null ? 0 : Integer.MIN_VALUE) : (textField.getValue() == null ? Integer.MAX_VALUE : getValue()
				.toString().compareTo(textField.getValue().toString()));
	}

}
