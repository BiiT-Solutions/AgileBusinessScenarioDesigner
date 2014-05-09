package com.biit.abcd.webpages;

@SuppressWarnings("rawtypes")
public enum WebMap {
	LOGIN_PAGE(Login.class),

	FORM_MANAGER(FormManager.class),
	
	TREE_DESIGNER(TreeDesigner.class),
	
	DIAGRAM_BUILDER(FormDiagramBuilder.class);

	private static WebMap loginPage = WebMap.LOGIN_PAGE;

	private static WebMap defaultPage = WebMap.FORM_MANAGER;

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
