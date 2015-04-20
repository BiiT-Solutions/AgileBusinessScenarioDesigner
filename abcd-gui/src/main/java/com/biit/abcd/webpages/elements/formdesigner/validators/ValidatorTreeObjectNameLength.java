package com.biit.abcd.webpages.elements.formdesigner.validators;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Validator;

public class ValidatorTreeObjectNameLength implements Validator {
	private static final long serialVersionUID = 4078897036098602662L;
	private static final int MAX_LENGTH = TreeObject.MAX_UNIQUE_COLUMN_LENGTH;

	@Override
	public void validate(Object value) throws InvalidValueException {
		if(value!=null && ((String)value).length() > MAX_LENGTH){
			throw new InvalidValueException(ServerTranslate.translate(LanguageCodes.CAPTION_NAME_TOO_LARGE));
		}
	}

}
