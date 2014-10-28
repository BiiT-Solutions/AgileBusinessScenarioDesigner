package com.biit.abcd.core.utils;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.utils.FormComparator;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;

/**
 * Compares two forms. Must be equals (but with different IDs and ComparationIds).
 */
public class FormVersionComparator extends FormComparator {

	@Override
	protected void compareVersions(Form form1, Form form2) throws FormNotEqualsException {
		if (form1.getVersion() + 1 != (int) form2.getVersion()) {
			throw new FormNotEqualsException("Form's versions are incorrect!");
		}
	}

	@Override
	protected void compareFormDates(Form form1, Form form2) throws FormNotEqualsException {
		// Previous version ends when the other starts, unless finish the same day. 
		if ((form1.getAvailableTo() == null && form2.getAvailableFrom() != null)
				|| (form1.getAvailableTo() != null && form2.getAvailableFrom() == null)
				|| ((form1.getAvailableTo() != null && form2.getAvailableFrom() != null) && form1.getAvailableTo()
						.getTime() < form2.getAvailableFrom().getTime())) {
			throw new FormNotEqualsException("Form's validTo and ValidFrom are incorrect!");
		}
	}

}
