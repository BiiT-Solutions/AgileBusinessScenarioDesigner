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
	
	FORM_TREE_PROPERTY_NAME("form.tree.property.name"),
	
	INFO_USER_LOG("info.userLog"),
	INFO_USER_SESSION_EXPIRED("info.userSessionExpired"),
	
	WARNING_TITLE("warning.title"),
	WARNING_PERMISSIONS("warning.permissions"),
	
	FORM_TABLE_COLUMN_NAME("formTable.column.name"),
	FORM_TABLE_COLUMN_VERSION("formTable.column.version"),
	FORM_TABLE_COLUMN_ACCESS("formTable.column.access"),
	FORM_TABLE_COLUMN_USEDBY("formTable.column.usedBy"),
	FORM_TABLE_COLUMN_CREATEDBY("formTable.column.createdBy"),
	FORM_TABLE_COLUMN_CREATIONDATE("formTable.column.creationDate"),
	FORM_TABLE_COLUMN_MODIFIEDBY("formTable.column.modifiedBy"),
	FORM_TABLE_COLUMN_MODIFICATIONDATE("formTable.column.modificationDate"),
	
	TREE_DESIGNER_CATEGORY_ADD("treeDesigner.category.add"),
	TREE_DESIGNER_GROUP_ADD("treeDesigner.group.add"),
	TREE_DESIGNER_QUESTION_ADD("treeDesigner.question.add"),
	TREE_DESIGNER_ANSWER_ADD("treeDesigner.answer.add"),
	
	BOTTOM_MENU_FORM_MANAGER("windowNewForm.window.title"),
	BOTTOM_MENU_TREE_DESIGNER("page.bottommenu.treeDesigner"),
	BOTTOM_MENU_DIAGRAM_DESIGNER("page.bottommenu.diagramDesigner"),
	BOTTOM_MENU_DROOLS_EDITOR("page.bottommenu.droolsEditor"),
	
	WINDOW_NEWFORM_WINDOW_TITLE("windowNewForm.name.textField"),
	WINDOW_NEWFORM_NAME_TEXTFIELD("windowNewForm.name.textField"),
	WINDOW_NEWFORM_SAVEBUTTON_LABEL("windowNewForm.savebutton.label"),
	WINDOW_NEWFORM_SAVEBUTTON_TOOLTIP("windowNewForm.savebutton.tooltip"),
	WINDOW_NEWFORM_CANCELBUTTON_LABEL("windowNewForm.cancelbutton.label"),
	
	ERROR_USER_PERMISSION("error.user.permission"),
	ERROR_ACCESS("error.access"),
	ERROR_BADUSERPSWD("error.login.badUserPassword"),
	ERROR_ENCRYPTINGPASSWORD("error.login.passwordEncrypt"),
	ERROR_TRYAGAIN("error.login.tryAgain"),
	ERROR_CONTACT("error.contact"),
	ERROR_USER_SERVICE("error.userService"),
	ERROR_UNEXPECTED_WEBSERVICE_ERROR("error.unexpectedWebserviceError"),
	ERROR_REPEATED_FORM_NAME("error.form.repeatedName"),
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
