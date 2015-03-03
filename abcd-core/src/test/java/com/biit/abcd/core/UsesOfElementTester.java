package com.biit.abcd.core;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.abcd.core.utils.UsesOfElement;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Test(groups = { "usesOfElement" })
public class UsesOfElementTester {

	@Test
	public void countElements() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly {
		Form form = FormUtils.createCompleteForm();
		UsesOfElement useOfElement = new UsesOfElement(form);

		Assert.assertEquals(12, useOfElement.getUsesOfElement(form.getChild("Category1")));
		Assert.assertEquals(6, useOfElement.getUsesOfElement(form.getChild("Category1/Group2/ChooseOne")));
		Assert.assertEquals(0, useOfElement.getUsesOfElement(form.getChild("Category1/Group2/question6")));
		Assert.assertEquals(0, useOfElement.getUsesOfElement(form.getChild("NoExiste/Inventado")));
		Assert.assertEquals(2, useOfElement.getUsesOfElement(form.getChild("Category1/Group2/ChooseOne/Answer1")));
		Assert.assertEquals(1, useOfElement.getUsesOfElement(form.getChild("Category1/Group2/ChooseOne/Answer2")));
	}
}
