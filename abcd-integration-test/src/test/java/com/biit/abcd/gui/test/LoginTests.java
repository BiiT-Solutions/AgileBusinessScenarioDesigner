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

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "login")
public class LoginTests extends AbcdTester {

	private static final String BADUSERNAME = "badUserName@bad.com";
	private static final String BAPSSWORD = "badPassword";
	private static final String NEW_FORM_NAME = "new_form_1";

	@Test
	public void testLoginLogout() {
		loginRead1();
		getFormManager().logOut();
	}

	@Test
	public void testLoginFail() {
		login(BADUSERNAME, BAPSSWORD);
		checkNotificationIsError(getNotification());
	}

	@Test
	public void testLoginWithRightsToManageForm(){
		loginFormAdmin1();
		getFormManager().createNewForm(NEW_FORM_NAME);
		getFormManager().deleteForm(1);
		getFormManager().logOut();
	}
	
	@Test
	public void testLoginWithRightsToManageButNotDeleteForm(){
		loginFormEdit1();
		Assert.assertTrue(getFormManager().getNewForm().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManager().closeNewPopover();
		Assert.assertNull(getFormManager().getRemoveForm());
		getFormManager().logOut();
	}

	@Test
	public void testLoginWithoutRightsToManageForm(){
		loginRead1();
		Assert.assertFalse(getFormManager().getNewForm().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManager().closeNewPopover();
		Assert.assertNull(getFormManager().getRemoveForm());
		getFormManager().logOut();
	}
}
