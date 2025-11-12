package com.biit.abcd.gui.test;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
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

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.gui.test.StatusPreservingTests.Scope;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;

@Test(groups = "formVariables")
public class FormVariablesTests extends AbcdTester {

	private static final String NEW_FORM_NAME = "new_form";
	private static final String VAR_0 = "var00";
	private static final String VAR_1 = "var01";
	private static final String VAR_2 = "var02";
	private static final String VAR_3 = "var03";
	private static final String VAR_4 = "var04";
	private static final String VAR_5 = "var05";
	private static final String VAR_6 = "var06";
	private static final String VAR_7 = "var07";
	private static final String VAR_8 = "var08";
	private static final String VAR_9 = "var09";
	private static final String VAR_10 = "var10";
	private static final String VAR_11 = "var11";
	private static final String DEF_VALUE_TEXT = "ipsum lorem";
	private static final String DEF_VALUE_NUMBER = "3.1";
	private static final String DEF_VALUE_DATE = "3/11/15";

	@Test
	public void createVariables() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();

		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		getFormDesigner().clickFormVariables();

		getFormVariables().addVariable(0, VAR_0, AnswerFormat.TEXT, Scope.FORM, DEF_VALUE_TEXT);
		getFormVariables().addVariable(1, VAR_1, AnswerFormat.NUMBER, Scope.FORM, DEF_VALUE_NUMBER);
		getFormVariables().addVariable(2, VAR_2, AnswerFormat.DATE, Scope.FORM, DEF_VALUE_DATE);

		getFormVariables().save();

		getFormVariables().addVariable(3, VAR_3, AnswerFormat.TEXT, Scope.CATEGORY, DEF_VALUE_TEXT);
		getFormVariables().addVariable(4, VAR_4, AnswerFormat.NUMBER, Scope.CATEGORY, DEF_VALUE_NUMBER);
		getFormVariables().addVariable(5, VAR_5, AnswerFormat.DATE, Scope.CATEGORY, DEF_VALUE_DATE);

		getFormVariables().save();

		getFormVariables().addVariable(6, VAR_6, AnswerFormat.TEXT, Scope.GROUP, DEF_VALUE_TEXT);
		getFormVariables().addVariable(7, VAR_7, AnswerFormat.NUMBER, Scope.GROUP, DEF_VALUE_NUMBER);
		getFormVariables().addVariable(8, VAR_8, AnswerFormat.DATE, Scope.GROUP, DEF_VALUE_DATE);

		getFormVariables().save();

		getFormVariables().addVariable(9, VAR_9, AnswerFormat.TEXT, Scope.QUESTION, DEF_VALUE_TEXT);
		getFormVariables().addVariable(10, VAR_10, AnswerFormat.NUMBER, Scope.QUESTION, DEF_VALUE_NUMBER);
		getFormVariables().addVariable(11, VAR_11, AnswerFormat.DATE, Scope.QUESTION, DEF_VALUE_DATE);

		getFormVariables().save();
		getFormVariables().goToFormManager();

		getFormManager().clickFormVariables();
		Assert.assertTrue(getFormVariables().getTextField(0, 3).getValue().equals(DEF_VALUE_TEXT));
		Assert.assertTrue(getFormVariables().getTextField(1, 3).getValue().equals(DEF_VALUE_NUMBER));
		Assert.assertTrue(getFormVariables().getTextField(2, 3).getValue().equals(DEF_VALUE_DATE));

		getFormVariables().goToFormManager();
		getFormManager().logOut();
		deleteAllForms();
	}

	@Test
	public void removeVariables() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();

		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		getFormDesigner().clickFormVariables();

		getFormVariables().addVariable(0, VAR_0, AnswerFormat.TEXT, Scope.FORM, DEF_VALUE_TEXT);
		getFormVariables().addVariable(1, VAR_1, AnswerFormat.NUMBER, Scope.FORM, DEF_VALUE_NUMBER);
		getFormVariables().addVariable(2, VAR_2, AnswerFormat.DATE, Scope.FORM, DEF_VALUE_DATE);
		getFormVariables().save();

		getFormDesigner().goToFormManager();
		getFormDesigner().clickFormVariables();

		getFormVariables().removeVariable(0);
		Assert.assertFalse(getFormVariables().getTextField(0, 3).getValue().equals(DEF_VALUE_TEXT));
		getFormVariables().save();

		getFormDesigner().goToFormManager();
		getFormManager().logOut();
		deleteAllForms();
	}

}
