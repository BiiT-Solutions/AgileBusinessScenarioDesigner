package com.biit.abcd.core;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import org.testng.Assert;
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
	public void countElements()
			throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException, ElementIsReadOnly {
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
