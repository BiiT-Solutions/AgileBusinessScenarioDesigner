package com.biit.abcd.language;

public enum LanguageCodes {
	
	LOGIN_CAPTION_EMAIL("login.caption.email"),
	LOGIN_CAPTION_PASSWORD("login.caption.password"),
	LOGIN_CAPTION_SIGN_IN("login.caption.signIn"),
	LOGIN_ERROR_EMAIL("login.error.email"),
	LOGIN_ERROR_PASSWORD("login.error.password"),
	LOGIN_ERROR_USER("error.login.user"),
	
	ABOUT_US_CAPTION("aboutUs.caption"),
	ABOUT_US_TOOL_NAME("aboutUs.toolName"),
	ABOUT_US_TOOL_PURPOUSE("aboutUs.toolPurpouse"),
	ABOUT_US_VERSION("aboutUs.version"),
	ABOUT_US_BIIT("aboutUs.biit"),
	ABOUT_US_REPORT("aboutUs.report"),
	
	INFO_USER_LOG("info.userLog"),
	INFO_USER_SESSION_EXPIRED("info.userSessionExpired"),
	
	WARNING_TITLE("warning.title"),
	WARNING_PERMISSIONS("warning.permissions"),
	
	ERROR_USER_PERMISSION("error.user.permission"),
	ERROR_ACCESS("error.access"),
	ERROR_BADUSERPSWD("error.login.badUserPassword"),
	ERROR_ENCRYPTINGPASSWORD("error.login.passwordEncrypt"),
	ERROR_TRYAGAIN("error.login.tryAgain"),
	ERROR_CONTACT("error.contact"),
	ERROR_USER_SERVICE("error.userService"),
	ERROR_UNEXPECTED_WEBSERVICE_ERROR("error.unexpectedWebserviceError"),
	ERROR_PORTLET_NOT_INSTALLED("error.portletNotInstalled");
	
	private String value;
	
	private LanguageCodes(String value){
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
