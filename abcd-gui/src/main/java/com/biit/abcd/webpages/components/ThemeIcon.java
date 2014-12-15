package com.biit.abcd.webpages.components;

import com.vaadin.server.ThemeResource;

public enum ThemeIcon {

	ACCEPT("button.accept.svg"),

	CANCEL("button.cancel.svg"),

	ADD("appbar.add.svg"),

	DELETE("element.delete.svg"),

	EDIT("element.edit.svg"),

	LINK("appbar.link.svg"),
	
	DOWNLOAD("file.download.svg"),

	FORM_SAVE("form.save.svg"),

	CONFIGURE("appbar.settings.svg"),

	SEARCH("appbar.magnify.svg"),

	ZOOM_IN("appbar.magnify.add.svg"),

	ZOOM_OUT("appbar.magnify.minus.svg"),

	PAGE_ADD("appbar.page.add.svg"),

	ARROW_LEFT("appbar.arrow.left.svg"),

	ARROW_RIGHT("appbar.arrow.right.svg"),

	USER("appbar.user.svg"),

	ELEMENT_EXPAND("element.expand.svg"),

	ELEMENT_COLLAPSE("element.collapse.svg"),

	LEFT_MENU_COLLAPSE("appbar.layout.collapse.left.variant.svg"),

	LEFT_MENU_EXPAND("appbar.layout.expand.right.variant.svg"),

	RIGHT_MENU_COLLAPSE("appbar.layout.collapse.right.variant.svg"),

	RIGHT_MENU_EXPAND("appbar.layout.expand.left.variant.svg"),

	FORM_MANAGER_PAGE("page.form.manager.svg"),

	FORM_MANAGER_ADD_FORM("form.create.svg"),
	
	FORM_MANAGER_FORM_NEW_VERSION("form.new.version.svg"),
	
	FORM_MANAGER_EXPORT_RULES("form.export.rules.svg"),

	TREE_DESIGNER_PAGE("page.form.edit.svg"),

	TREE_DESIGNER_ADD_CATEGORY("form.category.add.svg"),

	TREE_DESIGNER_ADD_QUESTION("form.question.add.svg"),

	TREE_DESIGNER_ADD_GROUP("form.group.add.svg"),

	TREE_DESIGNER_ADD_ANSWER("form.answer.add.svg"),

	TREE_DESIGNER_QUESTION_TYPE_DATE("question.calendar.31.svg"),

	TREE_DESIGNER_QUESTION_TYPE_TEXT("question.textbox.letters.svg"),

	TREE_DESIGNER_QUESTION_TYPE_NUMBER("question.textbox.numbers.svg"),

	TREE_DESIGNER_QUESTION_TYPE_CHECKLIST("question.checkmark.svg"),

	TREE_DESIGNER_QUESTION_TYPE_RADIOBUTTON("question.radiobutton.svg"),

	TREE_DESIGNER_QUESTION_TYPE_DROPDOWN("question.dropdown.svg"),

	TREE_DESIGNER_QUESTION_TYPE_POSTALCODE("question.postcode.svg"),

	TREE_DESIGNER_GROUP_LOOP("group.loop.svg"),

	DIAGRAM_BUILDER_PAGE("page.form.diagram.svg"),

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

	MOVE_UP("element.move.up.svg"),

	MOVE_DOWN("element.move.down.svg"),
	
	MOVE_TO("element.move.svg"),

	TABLE("appbar.table.svg"),

	TABLE_ADD("appbar.table.add.svg"),

	TABLE_REMOVE("appbar.table.remove.svg"),

	TABLE_ADD_COLUMN("appbar.table.add.column.svg"),

	TABLE_REMOVE_COLUMN("appbar.table.delete.column.svg"),

	ADD_ROW("appbar.table.add.row.svg"),

	REMOVE_ROW("appbar.table.delete.row.svg"),

	COPY_ROW("appbar.page.copy.svg"),

	PASTE_ROW("appbar.clipboard.paste.svg"),

	EXPRESSION_EDITOR_PAGE("appbar.page.expression.svg"),

	EXPRESSION_EDITOR_TAB_FUNCTIONS("appbar.cell.function.svg"),

	EXPRESSION_EDITOR_TAB_MATHS("appbar.calculator.svg"),

	EXPRESSION_EDITOR_TAB_GLOBAL_CONSTANTS("appbar.tag.svg"),

	EXPRESSION_EDITOR_TAB_FORM_VARIABLES("appbar.form.variables.svg"),

	EXPRESSION_EDITOR_TAB_FORM_GENERIC_VARIABLES("appbar.page.svg"),
	
	EXPRESSION_EDITOR_TAB_PLUGIN("plugin.svg"),

	SETTINGS("menu.lines.horizontal.svg"),

	ADD_ITEM_LIST("appbar.list.add.svg"),

	REMOVE_ITEM_LIST("appbar.list.delete.svg"),

	RULE_EDITOR_ADD_QUESTION("appbar.input.question.svg"),

	RULE_ADD("appbar.rule.add.svg"),

	RULE_REMOVE("appbar.rule.remove.svg"),

	EXPRESSION_ADD("appbar.expression.add.svg"),

	EXPRESSION_REMOVE("appbar.expression.remove.svg"),

	FORM_VARIABLE_EDITOR("appbar.form.variables.svg"),

	GLOBAL_VARIABLES_ADD_VARIABLE("appbar.notification.add.svg"),

	GLOBAL_VARIABLES_REMOVE_VARIABLE("appbar.notification.remove.svg"),

	GLOBAL_VARIABLES_EDIT_VARIABLE("appbar.notification.edit.svg"),

	GLOBAL_VARIABLES_ADD_VALUE("appbar.interface.textbox.watch.add.svg"),

	GLOBAL_VARIABLES_REMOVE_VALUE("appbar.interface.textbox.watch.remove.svg"),

	GLOBAL_VARIABLES_EDIT_VALUE("appbar.interface.textbox.watch.edit.svg"),

	FORM_VARIABLES("appbar.form.variables.svg"),

	FORM_VARIABLE_ADD("appbar.notification.add.svg"),

	FORM_VARIABLE_REMOVE("appbar.notification.remove.svg"),
	
	FORM_TEST_PAGE("page.form.tests.svg"),
	
	FORM_TEST_LAUNCH("form.tests.launch.svg"),
	
	TEST_ADD("page.form.tests.add.svg"),
	
	TEST_REMOVE("page.form.tests.remove.svg"),
	
	EXPORT_CSV_FILE("appbar.page.csv.svg"),
	
	TEST_COPY_GROUP("page.form.tests.remove.svg"),
	
	TEST_REMOVE_GROUP("page.form.tests.remove.svg"),
	
	FORM_FINISH("form.protect.svg");
	

	private String value;

	ThemeIcon(String value) {
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
