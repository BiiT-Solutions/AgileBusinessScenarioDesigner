package com.biit.abcd.webpages;

@SuppressWarnings("rawtypes")
public enum WebMap {
	LOGIN_PAGE(Login.class),

	MANAGE_FORM(FormManager.class);

	private static WebMap loginPage = WebMap.LOGIN_PAGE;

	private static WebMap defaultPage = WebMap.MANAGE_FORM;

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

}
