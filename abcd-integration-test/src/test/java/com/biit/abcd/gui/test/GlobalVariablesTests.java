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

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.NotificationElement;


@Test(groups = "globalVariables")
public class GlobalVariablesTests extends AbcdTester{

	private static final String VAR_1 = "var1";
	private static final String VAR_2 = "var2";
	private static final String VAR_3 = "var3";
	private static final String VAR_4 = "var4";
	private static final String TEXT_VAR_VALUE = "ASD";
	private static final String DATE_VAR_VALUE = "5/11/11";
	private static final String NUMBER_VAR_VALUE = "3.1";
	private static final String POSTAL_VAR_VALUE = "9999AA";
	private static final String VALID_FROM = "4/10/14";
	private static final String VALID_TO = "4/10/16";
	private static final String MOD = "_MOD";
	private static final String VALID_FROM_1 = "4/10/14";
	private static final String VALID_TO_1 = "5/10/14";
	private static final String VALID_TO_2 = "7/10/14";

	@Test
	public void createGlobalConstants() {
		
		login(ABCD_GLOBAL_CONST_BIIT1);
		
		getFormManager().goToGlobalVariables();
		getGlobalVariables().createGlobalVariable(VAR_1, AnswerFormat.TEXT, TEXT_VAR_VALUE, VALID_FROM, VALID_TO);
		getGlobalVariables().createGlobalVariable(VAR_2, AnswerFormat.DATE, DATE_VAR_VALUE, VALID_FROM, VALID_TO);
		getGlobalVariables().createGlobalVariable(VAR_3, AnswerFormat.NUMBER, NUMBER_VAR_VALUE, VALID_FROM, VALID_TO);
		getGlobalVariables().createGlobalVariable(VAR_4, AnswerFormat.POSTAL_CODE, POSTAL_VAR_VALUE, VALID_FROM, VALID_TO);

		getGlobalVariables().save();
	}
	
	@Test(dependsOnMethods={"createGlobalConstants"})
	public void tryToEditConstantType() {
		
		getGlobalVariables().clickRow(0);
		getGlobalVariables().editVariable();
		Assert.assertFalse(getGlobalVariables().getNewVariableWindow().isEnabledType());
		getGlobalVariables().getNewVariableWindow().clickCancel();

	}
	
	@Test(dependsOnMethods={"tryToEditConstantType"})
	public void editConstantNames() {
		
		getGlobalVariables().editVariable();
		getGlobalVariables().getNewVariableWindow().setName(VAR_1+MOD);
		getGlobalVariables().getNewVariableWindow().clickAccept();
		
		getGlobalVariables().clickRow(1);
		getGlobalVariables().editVariable();
		getGlobalVariables().getNewVariableWindow().setName(VAR_2+MOD);
		getGlobalVariables().getNewVariableWindow().clickAccept();
		
		getGlobalVariables().clickRow(2);
		getGlobalVariables().editVariable();
		getGlobalVariables().getNewVariableWindow().setName(VAR_3+MOD);
		getGlobalVariables().getNewVariableWindow().clickAccept();
		
		getGlobalVariables().clickRow(3);
		getGlobalVariables().editVariable();
		getGlobalVariables().getNewVariableWindow().setName(VAR_4+MOD);
		getGlobalVariables().getNewVariableWindow().clickAccept();

		getGlobalVariables().save();
	}
	
	@Test(dependsOnMethods={"editConstantNames"})
	public void removeConstant() {
		
		getGlobalVariables().clickRow(0);
		getGlobalVariables().removeVariable();
		getGlobalVariables().clickRow(0);
		getGlobalVariables().removeVariable();
		getGlobalVariables().clickRow(0);
		getGlobalVariables().removeVariable();
		getGlobalVariables().clickRow(0);
		getGlobalVariables().removeVariable();
		
		getGlobalVariables().save();
	}
	
	@Test(dependsOnMethods={"removeConstant"})
	public void testMissingValidToAndCorrect() {
		
		getGlobalVariables().createGlobalVariable(VAR_1, AnswerFormat.TEXT, TEXT_VAR_VALUE, VALID_FROM, null);
		getGlobalVariables().save();
		getGlobalVariables().clickAddNewValue();
		
		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsWarning(notification);
		notification.close();

		getGlobalVariables().clickEditValue();
		getGlobalVariables().getAddNewValueWindow().setValidTo(VALID_TO);
		getGlobalVariables().getAddNewValueWindow().clickAccept();
		
		getGlobalVariables().clickAddNewValue();		
		Assert.assertFalse($(NotificationElement.class).exists());
		getGlobalVariables().getAddNewValueWindow().clickCancel();
		
		getGlobalVariables().save();
		
		//Clean
		getGlobalVariables().removeVariable();
		getGlobalVariables().save();
	}
	
	@Test(dependsOnMethods={"testMissingValidToAndCorrect"})
	public void testTryToRemoveMiddleValue(){
		
		getGlobalVariables().createGlobalVariable(VAR_1, AnswerFormat.TEXT, TEXT_VAR_VALUE, VALID_FROM_1, VALID_TO_1);
		getGlobalVariables().addNewValue(TEXT_VAR_VALUE, null, VALID_TO_2);
		getGlobalVariables().addNewValue(TEXT_VAR_VALUE, null, null);
		
		getGlobalVariables().selectValue(1);
		getGlobalVariables().clickRemoveValue();
		
		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsWarning(notification);
		notification.close();
		
		getGlobalVariables().selectValue(2);
		getGlobalVariables().clickRemoveValue();
		
		getGlobalVariables().selectValue(1);
		getGlobalVariables().clickEditValue();
		getGlobalVariables().getAddNewValueWindow().setValidTo("");
		getGlobalVariables().getAddNewValueWindow().clickAccept();
		
		getGlobalVariables().save();
		
		//Clean
		getGlobalVariables().removeVariable();
		getGlobalVariables().save();
	}
	
}
