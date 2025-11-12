package com.biit.abcd.webpages;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

@SuppressWarnings("rawtypes")
public enum WebMap {
	
	ERROR_PAGE(ErrorPage.class),
	
	NOT_FOUND_PAGE(NotFoundPage.class),
	
	LOGIN_PAGE(Login.class),

	FORM_MANAGER(FormManager.class),

	TREE_DESIGNER(FormDesigner.class),

	FORM_VARIABLES(FormVariables.class),

	DIAGRAM_BUILDER(FormDiagramBuilder.class),

	DECISSION_TABLE_EDITOR(TableRuleEditor.class),

	EXPRESSION_EDITOR(ExpressionEditor.class),

	DROOLS_RULE_EDITOR(DroolsRuleEditor.class),

	GLOBAL_VARIABLES(GlobalVariablesCreator.class),

	TEST_SCENARIOS(TestScenarioEditor.class),
	
	;

	private static WebMap loginPage = WebMap.LOGIN_PAGE;

	private static WebMap defaultPage = WebMap.FORM_MANAGER;
	
	private static WebMap errorPage = WebMap.ERROR_PAGE;
	
	private static WebMap notFoundPage = WebMap.NOT_FOUND_PAGE;

	private Class redirectTo;

	WebMap(Class redirectTo) {
		this.redirectTo = redirectTo;
	}

	public Class getWebPageJavaClass() {
		return redirectTo;
	}

	public static WebMap getLoginPage() {
		return loginPage;
	}

	public static WebMap getMainPage() {
		return defaultPage;
	}

	public static WebMap getNotFoundPage() {
		return notFoundPage;
	}

	public static WebMap getErrorPage() {
		return errorPage;
	}

}
