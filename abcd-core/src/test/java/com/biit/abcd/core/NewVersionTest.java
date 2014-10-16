package com.biit.abcd.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Test(groups = { "newVersion" })
public class NewVersionTest {

	@Autowired
	private IFormDao formDao;

	private Form form;

	@Test
	public void createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException {
		form = FormUtils.createCompleteForm();
	}

}
