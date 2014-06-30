package com.biit.abcd.webpages.components;

import com.vaadin.server.ThemeResource;

public enum ThemeIcons {

	ACCEPT("appbar.check.svg"),

	CANCEL("appbar.close.svg"),

	ADD("appbar.add.svg"),

	DELETE("appbar.delete.svg"),

	EDIT("appbar.edit.svg"),

	LINK("appbar.link.svg"),

	SAVE("appbar.save.svg"),

	CONFIGURE("appbar.settings.svg"),

	SEARCH("appbar.magnify.svg"),

	ZOOM_IN("appbar.magnify.add.svg"),

	ZOOM_OUT("appbar.magnify.minus.svg"),

	PAGE_ADD("appbar.page.add.svg"),

	ARROW_LEFT("appbar.arrow.left.svg"),

	ARROW_RIGHT("appbar.arrow.right.svg"),

	USER("appbar.user.svg"),

	EXPAND("appbar.section.expand.svg"),

	COLLAPSE("appbar.section.collapse.svg"),

	LEFT_MENU_COLLAPSE("appbar.layout.collapse.left.variant.svg"),

	LEFT_MENU_EXPAND("appbar.layout.expand.right.variant.svg"),

	RIGHT_MENU_COLLAPSE("appbar.layout.collapse.right.variant.svg"),

	RIGHT_MENU_EXPAND("appbar.layout.expand.left.variant.svg"),

	FORM_MANAGER_PAGE("appbar.cabinet.files.svg"),

	FORM_MANAGER_ADD_FORM("appbar.page.add.svg"),

	TREE_DESIGNER_PAGE("appbar.page.edit.svg"),

	TREE_DESIGNER_ADD_CATEGORY("appbar.category.add.svg"),

	TREE_DESIGNER_ADD_QUESTION("appbar.question.add.svg"),

	TREE_DESIGNER_ADD_GROUP("appbar.group.add.svg"),

	TREE_DESIGNER_ADD_ANSWER("appbar.answer.add.svg"),
	
	TREE_DESIGNER_QUESTION_DATE("appbar.calendar.31.svg"),
	
	TREE_DESIGNER_QUESTION_TEXT("appbar.interface.textbox.svg"),
	
	TREE_DESIGNER_QUESTION_NUMBER("appbar.interface.textbox.number.svg"),
	
	TREE_DESIGNER_QUESTION_CHECKLIST("appbar.checkmark.svg"),
	
	TREE_DESIGNER_QUESTION_RADIOBUTTON("appbar.radiobutton.svg"),
	
	TREE_DESIGNER_QUESTION_DROPDOWN("appbar.interface.dropdown.svg"),
	
	TREE_DESIGNER_QUESTION_POSTALCODE("appbar.email.hardedge.svg"),

	DIAGRAM_BUILDER_PAGE("appbar.diagram.svg"),

	DROOLS_RULE_EDITOR_PAGE("appbar.page.code.svg"),

	ADD_DIAGRAM("appbar.diagram.add.svg"),

	REMOVE_DIAGRAM("appbar.diagram.remove.svg"),

	CLEAN("appbar.clean.svg"),

	UNDO("appbar.undo.svg"),

	REDO("appbar.redo.svg"),

	TO_PNG("appbar.page.png.svg"),

	TO_SVG("appbar.page.svg.svg"),

	TO_BACK("appbar.toBack.svg"),

	TO_FRONT("appbar.toFront.svg"),

	MOVE_UP("appbar.chevron.up.svg"),

	MOVE_DOWN("appbar.chevron.down.svg"),

	TABLE("appbar.table.svg"),

	TABLE_ADD("appbar.table.add.svg"),

	TABLE_REMOVE("appbar.table.remove.svg"),

	TABLE_ADD_COLUMN("appbar.table.add.column.svg"),

	TABLE_REMOVE_COLUMN("appbar.table.delete.column.svg"),

	ADD_ROW("appbar.table.add.row.svg"),

	REMOVE_ROW("appbar.table.delete.row.svg"),

	EXPRESSION_EDITOR_PAGE("appbar.page.expression.svg"),

	SETTINGS("appbar.lines.horizontal.4.svg"),

	ADD_ITEM_LIST("appbar.list.add.svg"),

	REMOVE_ITEM_LIST("appbar.list.delete.svg"),

	RULE_EDITOR_ADD_QUESTION("appbar.input.question.svg"),
	
	RULE_ADD("appbar.rule.add.svg"),
	
	RULE_REMOVE("appbar.rule.remove.svg"),

	EXPRESSION_ADD("appbar.expression.add.svg"),

	EXPRESSION_REMOVE("appbar.expression.remove.svg"),

	FORM_VARIABLE_EDITOR("appbar.form.variables.svg"),

	GLOBAL_VARIABLES_ADD_VARIABLE("appbar.list.add.below.svg"),

	GLOBAL_VARIABLES_REMOVE_VARIABLE("appbar.list.delete.inline.svg"),

	GLOBAL_VARIABLES_ADD_VALUE("appbar.interface.textbox.svg"),

	GLOBAL_VARIABLES_REMOVE_VALUE("appbar.interface.textbox.remove.svg"),

	FORM_VARIABLES("appbar.form.variables.svg"),

	VARIABLE_ADD("appbar.notification.add.svg"),

	VARIABLE_REMOVE("appbar.notification.remove.svg");

	private String value;

	ThemeIcons(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public ThemeResource getThemeResource() {
		return new ThemeResource(value);
	}

	public String getFile() {
		return value;
	}
}
