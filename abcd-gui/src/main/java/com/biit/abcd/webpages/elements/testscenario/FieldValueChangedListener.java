package com.biit.abcd.webpages.elements.testscenario;

import com.vaadin.ui.Field;

public interface FieldValueChangedListener {
	/**
	 * Field value can be null!
	 * 
	 * @param field
	 */
	public void valueChanged(Field<?> field);
}
