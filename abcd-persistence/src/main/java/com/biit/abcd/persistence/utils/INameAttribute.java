package com.biit.abcd.persistence.utils;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Interface created to allow to retrieve the name of the classes that implement
 * it
 * 
 */
public interface INameAttribute {

	public String getName();

	public void setName(String name) throws FieldTooLongException, CharacterNotAllowedException;

	@Override
	public String toString();
}
